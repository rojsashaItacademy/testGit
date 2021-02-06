package ru.trinitydigital.pagingcashe.ui.without_cashe

import androidx.paging.PagingSource
import retrofit2.HttpException
import ru.trinitydigital.pagingcashe.data.START_PAGE
import ru.trinitydigital.pagingcashe.data.model.RowsModel
import ru.trinitydigital.pagingcashe.data.remote.CoursesService
import ru.trinitydigital.pagingcashe.data.repositories.PagingRepositories.Companion.PAGE_SIZE
import java.io.IOException

private const val PARAM_QUERY = "in:name,description"

class WithoutCachePagingSource(
    private val service: CoursesService,
    private val query: String
) : PagingSource<Int, RowsModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RowsModel> {

        val page = params.key ?: START_PAGE
        val apiQuery = query + PARAM_QUERY

        return try {
            val response = service.getCourses(query = apiQuery, page = page, params.loadSize)
            val itemsCount = response.items.size

            val nextPage = if (itemsCount == 0) {
                null
            } else {
                page + (params.loadSize / PAGE_SIZE)// page =+ 1
            }

            LoadResult.Page(
                data = response.items,
                prevKey = if (page == START_PAGE) null else page - 1,
                nextKey = nextPage
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}