package ru.trinitydigital.pagingcashe.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import ru.trinitydigital.pagingcashe.data.PagingMediator
import ru.trinitydigital.pagingcashe.data.db.PagingCasheAppDatabase
import ru.trinitydigital.pagingcashe.data.model.RowsModel
import ru.trinitydigital.pagingcashe.data.remote.CoursesService

class PagingRepositories(
    private val service: CoursesService,
    private val db: PagingCasheAppDatabase) {

    @ExperimentalPagingApi
    fun getPagingResult(): LiveData<PagingData<RowsModel>> {
        val pagingSourceFactory = { db.getPagingCasheDao().getAll() }

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PagingMediator(service, db),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}