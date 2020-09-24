package com.vandenbreemen.modernsimmingapp.di.hilt

import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsAPI
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GooglePostContentLoader
import com.vandenbreemen.modernsimmingapp.data.repository.GoogleGroupsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class RepositoriesModule {

    @Provides
    fun providesGoogleGroupsRepository(googleGroupsAPI: GoogleGroupsAPI): GoogleGroupsRepository {
        return GoogleGroupsRepository(
            googleGroupsAPI,
            GooglePostContentLoader()
        )
    }

}