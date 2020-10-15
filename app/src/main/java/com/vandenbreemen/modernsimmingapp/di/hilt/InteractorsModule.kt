package com.vandenbreemen.modernsimmingapp.di.hilt

import android.content.Context
import com.vandenbreemen.modernsimmingapp.config.ConfigInteractor
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.data.repository.GoogleGroupsRepository
import com.vandenbreemen.modernsimmingapp.fetcher.FetchPostInteractor
import com.vandenbreemen.modernsimmingapp.fetcher.PostManagementInteractor
import com.vandenbreemen.modernsimmingapp.services.ServicesInteractor
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class, ActivityComponent::class, FragmentComponent::class)
class InteractorsModule {

    @Provides
    fun providesFetchPostInteractor(googleGroupsRepository: GoogleGroupsRepository, postsDatabase: PostsDatabase): FetchPostInteractor {
        return FetchPostInteractor(googleGroupsRepository, postsDatabase)
    }

    @Provides
    fun providesTTSInteractor(@ApplicationContext context: Context): TTSInteractor {
        return TTSInteractorImpl(context)
    }

    @Provides
    fun providesPostManagementInteractor(postsDatabase: PostsDatabase): PostManagementInteractor {
        return PostManagementInteractor(postsDatabase.postDao())
    }

    @Provides
    fun providesSimContentProviderInteractor(@ActivityContext context: Context): SimContentProviderInteractor {
        return SimContentProviderInteractor(context)
    }

    @Provides
    fun providesServicesInteractor(@ApplicationContext context: Context): ServicesInteractor {
        return ServicesInteractor(context)
    }

    @Provides
    fun providesConfigInteractor(@ApplicationContext context: Context): ConfigInteractor {
        return ConfigInteractor(context)
    }

}