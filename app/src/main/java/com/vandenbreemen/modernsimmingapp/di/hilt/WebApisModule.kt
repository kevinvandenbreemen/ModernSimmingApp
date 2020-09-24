package com.vandenbreemen.modernsimmingapp.di.hilt

import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

const val GOOGLE_GROUPS_BASE_URL = "https://groups.google.com/"

@Module
@InstallIn(SingletonComponent::class)
class WebApisModule {

    @Provides
    @Singleton
    fun providesGoogleGroupsAPI(): GoogleGroupsAPI {
        return Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build().create(
                GoogleGroupsAPI::class.java
            )
    }

}