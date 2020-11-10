package com.vandenbreemen.modernsimmingapp.di.hilt

import com.vandenbreemen.modernsimmingapp.broadcast.Broadcaster
import com.vandenbreemen.modernsimmingapp.config.ConfigInteractor
import com.vandenbreemen.modernsimmingapp.config.SharedPreferencesInteractor
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.fetcher.FetchPostInteractor
import com.vandenbreemen.modernsimmingapp.fetcher.PostManagementInteractor
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@EntryPoint
@InstallIn(ApplicationComponent::class)
interface BackendEntryPoint {


    fun getFetchPostsInteractor(): FetchPostInteractor
    fun getPostsDatabase(): PostsDatabase
    fun getBroadcaster(): Broadcaster
    fun getTTSInteractor(): TTSInteractor
    fun getPostManagementInteractor(): PostManagementInteractor
    fun getSharedPreferencesInteractor(): SharedPreferencesInteractor
    fun getConfigInteractor(): ConfigInteractor

}