package com.vandenbreemen.modernsimmingapp.data.localstorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity
@Fts4(notIndexed = ["postId"])
data class PostContent (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    val id: Int,

    val postId: Int,

    @ColumnInfo(name = "content")
    val content: String

)