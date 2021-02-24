package ru.trinitydigital.pagingcashe.ui.without_cashe

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.trinitydigital.pagingcashe.data.model.RowsModel
import ru.trinitydigital.pagingcashe.data.model.TestEntitiy
import ru.trinitydigital.pagingcashe.data.model.TestListItem
import ru.trinitydigital.pagingcashe.data.model.TestModel
import ru.trinitydigital.pagingcashe.data.remote.CoursesService
import ru.trinitydigital.pagingcashe.data.repositories.PagingRepositories
import kotlin.random.Random

class WithoutCacheViewModel(
    private val repo: PagingRepositories,
    private val service: CoursesService
) : ViewModel() {

    val listData = MutableLiveData<ArrayList<RateData>>()
    var list = ArrayList<RateData>()
    val eventUpdateLikes = MutableLiveData<Int>()
    val testLiveData = MutableLiveData<TestEntitiy>()

    init {
        generateData()
    }

    fun getLikes(pos: Int) {
        list[pos].rate = Random.nextInt(1000, 1000000)
        listData.postValue(list)
        Handler(Looper.getMainLooper()).postDelayed({
            eventUpdateLikes.postValue(pos)
        }, 500)
    }

    @ExperimentalPagingApi
    fun getPagingData(query: String): Flow<PagingData<RowsModel>> {
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("asdasdasd", "asdasdasdsad")
        Log.d("asdasdasd", "asdasdasdsad")
        return repo.getPagingForSearch(query)
    }

    fun generateData() {
        val list = arrayListOf<RateData>()
        for (i in 1..50) {
            list.add(RateData(rate = Random.nextInt(10, 1000000)))
        }
        this.list.addAll(list)
        listData.postValue(list)
    }

    fun updateRateAdapter(pos: Int, last: Int) {
        val localList = ArrayList<RateData>()
        localList.addAll(list)
        if (last >= 0) {
            val item = localList[last].copy()
            item.isSelected = false
            localList[last] = item
        }

        val item = localList[pos].copy()
        item.isSelected = true
        localList[pos] = item
        listData.postValue(localList)
    }

    fun loadCounties() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val result = service.getCountries()
                parseJsonObject(result)
            }.onFailure { error ->
                Log.d("Asdasdasdas", error?.localizedMessage)
            }
        }
    }

    fun parseJsonObject(result: TestModel) {
        val keys = result.data.keySet().toList()
        val list = arrayListOf<TestListItem>()

        if (keys != null) {
            for (item in keys) {
                val value = result.data.get(item).toString()
                val json = Gson().fromJson(value, TestListItem::class.java)
                list.add(json)
            }
        }
        testLiveData.postValue(TestEntitiy(list))
    }
}