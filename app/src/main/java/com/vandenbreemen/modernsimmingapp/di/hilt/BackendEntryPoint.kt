package com.vandenbreemen.modernsimmingapp.di.hilt

import com.vandenbreemen.modernsimmingapp.broadcast.Broadcaster
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.fetcher.FetchPostInteractor
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@EntryPoint
@InstallIn(ApplicationComponent::class)
interface BackendEntryPoint {


    fun getFetchPostsInteractor(): FetchPostInteractor
    fun getPostsDatabase(): PostsDatabase
    fun getBroadcaster(): Broadcaster

}