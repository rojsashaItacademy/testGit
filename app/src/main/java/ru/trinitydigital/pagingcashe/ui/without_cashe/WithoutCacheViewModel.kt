package ru.trinitydigital.pagingcashe.ui.without_cashe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.trinitydigital.pagingcashe.data.model.RowsModel
import ru.trinitydigital.pagingcashe.data.repositories.PagingRepositories

class WithoutCacheViewModel(private val repo: PagingRepositories) : ViewModel() {

    @ExperimentalPagingApi
    fun getPagingData(query: String): Flow<PagingData<RowsModel>> {
        return repo.getPagingForSearch(query)
    }
}