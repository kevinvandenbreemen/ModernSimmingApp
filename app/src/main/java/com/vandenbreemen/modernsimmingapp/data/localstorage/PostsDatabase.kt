package com.vandenbreemen.modernsimmingapp.data.localstorage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version = 1)
abstract class PostsDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

}