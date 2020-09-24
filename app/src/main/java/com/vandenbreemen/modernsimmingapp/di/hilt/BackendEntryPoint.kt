package com.vandenbreemen.modernsimmingapp.di.hilt

import com.vandenbreemen.modernsimmingapp.fetcher.FetchPostInteractor
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@EntryPoint
@InstallIn(ApplicationComponent::class)
interface BackendEntryPoint {


    fun getFetchPostsInteractor(): FetchPostInteractor

}