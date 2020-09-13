package com.vandenbreemen.modernsimmingapp.data.localstorage

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    indices = [Index("url", unique = true)],
    foreignKeys = [ForeignKey(entity = Group::class, parentColumns = ["id"], childColumns = ["groupId"], onDelete = CASCADE)]
)
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