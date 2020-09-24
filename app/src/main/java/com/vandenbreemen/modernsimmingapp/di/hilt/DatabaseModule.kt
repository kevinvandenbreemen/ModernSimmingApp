package com.vandenbreemen.modernsimmingapp.di.hilt

import android.content.Context
import androidx.room.Room
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun providesPostsDatabase(@ApplicationContext context: Context): PostsDatabase {
        return Room.databaseBuilder(context, PostsDatabase::class.java, "PostDB").build()
    }

}