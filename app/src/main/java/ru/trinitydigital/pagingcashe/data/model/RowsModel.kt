package ru.trinitydigital.pagingcashe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RowsModel(
    @PrimaryKey(autoGenerate = true)
    val sortId: Long,
    val id: Long,
    val name: String,
    val full_name: String,
    val description: String,
    val html_url: String,
    val stargazed_count: Int,
    val forks_count: Int,
    val language: String?,
)