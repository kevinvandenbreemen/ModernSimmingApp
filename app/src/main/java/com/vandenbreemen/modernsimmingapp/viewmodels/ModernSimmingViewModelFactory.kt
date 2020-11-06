package com.vandenbreemen.modernsimmingapp.viewmodels

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vandenbreemen.modernsimmingapp.di.hilt.FrontEndEntryPoint
import com.vandenbreemen.modernsimmingapp.navigation.NavigationViewModel
import dagger.hilt.android.EntryPointAccessors

class ModernSimmingViewModelFactory(private val frontEndEntryPoint: FrontEndEntryPoint): ViewModelProvider.Factory {

    companion object {
        fun fromActivity(activity: Activity): ModernSimmingViewModelFactory {
            return ModernSimmingViewModelFactory(EntryPointAccessors.fromActivity(activity, FrontEndEntryPoint::class.java))
        }

        fun fromFragment(fragment: Fragment): ModernSimmingViewModelFactory {
            return ModernSimmingViewModelFactory(EntryPointAccessors.fromFragment(fragment, FrontEndEntryPoint::class.java))
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            return frontEndEntryPoint.getOnboardingViewModel() as T
        }
        if(modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return frontEndEntryPoint.getOverviewViewModel() as T
        }
        if(modelClass.isAssignableFrom(PostListViewModel::class.java)) {
            return frontEndEntryPoint.getPostListViewModel() as T
        }
        if(modelClass.isAssignableFrom(PlaybackViewModel::class.java)) {
            return frontEndEntryPoint.getPlaybackViewModel() as T
        }
        if(modelClass.isAssignableFrom(NavigationViewModel::class.java)) {
            return frontEndEntryPoint.getNavigationViewModel() as T
        }

        throw IllegalArgumentException("Type $modelClass not supported")
    }
}