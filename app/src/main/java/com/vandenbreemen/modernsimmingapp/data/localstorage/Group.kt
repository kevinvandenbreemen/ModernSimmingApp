package com.vandenbreemen.modernsimmingapp.data.localstorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "group", indices = [Index(value=["name"], unique = true)])
data class Group(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo
    val name: String
)