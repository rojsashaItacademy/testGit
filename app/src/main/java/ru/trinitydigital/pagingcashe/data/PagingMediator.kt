package ru.trinitydigital.pagingcashe.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.processNextEventInCurrentThread
import retrofit2.HttpException
import ru.trinitydigital.pagingcashe.data.db.PagingCasheAppDatabase
import ru.trinitydigital.pagingcashe.data.model.PageKeys
import ru.trinitydigital.pagingcashe.data.model.RowsModel
import ru.trinitydigital.pagingcashe.data.remote.CoursesService
import java.io.IOException
import java.io.InvalidObjectException

const val START_PAGE = 1

@ExperimentalPagingApi
class PagingMediator(
    private val service: CoursesService,
    private val db: PagingCasheAppDatabase
) : RemoteMediator<Int, RowsModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RowsModel>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)

                remoteKeys?.nextKey?.minus(1) ?: START_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("prepend error ")
                val prevKey = remoteKeys.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys =
                    getRemoteKeyForLastItem(state) ?: throw InvalidObjectException("prepend error ")
                if (remoteKeys.nextKey == null) throw InvalidObjectException("prepend error ")

                remoteKeys.nextKey
            }
        }

        try {
            val apiResponse =
                service.getCourses("androidin:name,description", page, state.config.pageSize)
            val endOfPaginationReached = apiResponse.items.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getPagingCasheDao().deleteAll()
                    db.getPagingKeysDao().deleteAll()
                }
                val prevKey = if (page == START_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = apiResponse.items.map {
                    PageKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                db.getPagingKeysDao().insertAll(keys)
                db.getPagingCasheDao().insert(apiResponse.items)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, RowsModel>
    ): PageKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.getPagingKeysDao().getKeyId(repoId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RowsModel>): PageKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                db.getPagingKeysDao().getKeyId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RowsModel>): PageKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                db.getPagingKeysDao().getKeyId(repo.id)
            }
    }
}