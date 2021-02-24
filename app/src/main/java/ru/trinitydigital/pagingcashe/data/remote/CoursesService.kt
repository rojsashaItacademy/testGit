package ru.trinitydigital.pagingcashe.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import ru.trinitydigital.pagingcashe.data.model.BaseListingModel
import ru.trinitydigital.pagingcashe.data.model.RowsModel
import ru.trinitydigital.pagingcashe.data.model.TestModel

interface CoursesService {

    @GET("search/repositories?sort=stars")
    suspend fun getCourses(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): BaseListingModel<RowsModel>


    @GET
    suspend fun getCountries(
        @Url query: String = "https://app.sportdataapi.com/api/v1/soccer/countries?apikey=91edefc0-74f2-11eb-b8af-b7d03964d7a1&continent=Europe",
    ): TestModel
}