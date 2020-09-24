package com.vandenbreemen.modernsimmingapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ModernSimmingApp: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}