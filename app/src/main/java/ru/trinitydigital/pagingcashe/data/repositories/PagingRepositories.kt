package ru.trinitydigital.pagingcashe.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import ru.trinitydigital.pagingcashe.data.PagingMediator
import ru.trinitydigital.pagingcashe.data.db.PagingCasheAppDatabase
import ru.trinitydigital.pagingcashe.data.model.RowsModel
import ru.trinitydigital.pagingcashe.data.remote.CoursesService
import ru.trinitydigital.pagingcashe.ui.without_cashe.WithoutCachePagingSource

class PagingRepositories(
    private val service: CoursesService,
    private val db: PagingCasheAppDatabase
) {

    @ExperimentalPagingApi
    fun getPagingResult(): LiveData<PagingData<RowsModel>> {
        val pagingSourceFactory = { db.getPagingCasheDao().getAll() }

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PagingMediator(service, db),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }

    @ExperimentalPagingApi
    fun getPagingForSearch(query: String): Flow<PagingData<RowsModel>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true),
            pagingSourceFactory = { WithoutCachePagingSource(service, query) }
        ).flow
    }


    companion object {
        const val PAGE_SIZE = 20
    }
}