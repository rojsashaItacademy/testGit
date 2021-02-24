package ru.trinitydigital.pagingcashe.data.model

import com.google.gson.annotations.SerializedName

data class TestListItem(
    @SerializedName("country_id")
    val countryId: Int?,
    val name: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    val continent: String?
)