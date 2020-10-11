package com.vandenbreemen.modernsimmingapp.viewmodels

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vandenbreemen.modernsimmingapp.di.hilt.FrontEndEntryPoint
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

        throw IllegalArgumentException("Type $modelClass not supported")
    }
}