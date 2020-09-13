package com.vandenbreemen.modernsimmingapp.data.localstorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Group::class, parentColumns = ["id"], childColumns = ["groupId"], onDelete = CASCADE)])
data class Post(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "post_date")
    val postedDate: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "url")
    val url: String? = null,

    val groupId: Int? = null,
    var contentId: Int? = null
)