package com.vandenbreemen.modernsimmingapp.di.hilt

import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.data.repository.GoogleGroupsRepository
import com.vandenbreemen.modernsimmingapp.fetcher.FetchPostInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class InteractorsModule {

    @Provides
    fun providesFetchPostInteractor(googleGroupsRepository: GoogleGroupsRepository, postsDatabase: PostsDatabase): FetchPostInteractor {
        return FetchPostInteractor(googleGroupsRepository, postsDatabase)
    }

}