package com.vandenbreemen.modernsimmingapp.di.hilt

import com.vandenbreemen.modernsimmingapp.viewmodels.OnboardingViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent

@EntryPoint
@InstallIn(ActivityComponent::class, FragmentComponent::class)
interface FrontEndEntryPoint {

    fun getOnboardingViewModel(): OnboardingViewModel

}