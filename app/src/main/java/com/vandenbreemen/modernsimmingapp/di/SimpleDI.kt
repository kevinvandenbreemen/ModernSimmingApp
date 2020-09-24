package com.vandenbreemen.modernsimmingapp.di

import android.content.Context
import androidx.room.Room
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsAPI
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GooglePostContentLoader
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.data.repository.GoogleGroupsRepository
import com.vandenbreemen.modernsimmingapp.di.hilt.GOOGLE_GROUPS_BASE_URL
import com.vandenbreemen.modernsimmingapp.fetcher.FetchPostInteractor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

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

    fun getFetchPostInteractor(context: Context): FetchPostInteractor {
        val googleGroupsApi = Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build().create(
                GoogleGroupsAPI::class.java)

        return FetchPostInteractor(
            GoogleGroupsRepository(googleGroupsApi, GooglePostContentLoader()),
            getPostsDatabase(context)
        )
    }

    fun getPostsDatabase(context: Context): PostsDatabase {
        return SimpleDI.getPostDatabase(context)
    }

}