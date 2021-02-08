package ru.trinitydigital.pagingcashe.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import ru.trinitydigital.pagingcashe.data.model.RowsModel
import ru.trinitydigital.pagingcashe.data.repositories.PagingRepositories

class MainViewModel(private val repo: PagingRepositories) : ViewModel() {

    @ExperimentalPagingApi
    fun getPagingData(): LiveData<PagingData<RowsModel>> {
        Log.d("asdasdasd", "asdasdasdsad")
        return repo.getPagingResult()
    }
}