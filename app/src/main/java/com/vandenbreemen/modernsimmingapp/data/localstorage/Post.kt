package com.vandenbreemen.modernsimmingapp.data.localstorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity
@Fts4(notIndexed = ["post_date"])
data class Post(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Int,

    @ColumnInfo(name = "post_date")
    val postedDate: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "url")
    val url: String? = null
)