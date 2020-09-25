package com.vandenbreemen.modernsimmingapp.di.hilt

import android.content.Context
import com.vandenbreemen.modernsimmingapp.broadcast.Broadcaster
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
class BroadcastModule {

    @Provides
    fun providesBroadcaster(@ApplicationContext context: Context): Broadcaster {
        return Broadcaster(context)
    }

}