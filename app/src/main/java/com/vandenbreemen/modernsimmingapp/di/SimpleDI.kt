package com.vandenbreemen.modernsimmingapp.di

import android.content.Context
import androidx.room.Room
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase

class SimpleDI {

    companion object {
        private lateinit var postDatabase: PostsDatabase
        private fun getPostDatabase(context: Context): PostsDatabase {
            if(!::postDatabase.isInitialized){
                postDatabase = Room.databaseBuilder(context, PostsDatabase::class.java, "PostDB").build()
            }
            return postDatabase
        }
    }

    fun getPostsDatabase(context: Context): PostsDatabase {
        return SimpleDI.getPostDatabase(context)
    }

}