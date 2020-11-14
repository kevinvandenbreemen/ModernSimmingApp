package com.vandenbreemen.modernsimmingapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ModernSimmingApp: Application() {

    companion object {
        const val NOTIFICATION_ID = "NotificationChannel.ModernSimmingApp"
    }

    override fun onCreate() {
        super.onCreate()
        setupNotificationChannel()
    }

    fun setupNotificationChannel() {
        val notificationChannel = NotificationChannel(NOTIFICATION_ID, getString(R.string.notification_name), NotificationManager.IMPORTANCE_DEFAULT)
        (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.run {
            createNotificationChannel(notificationChannel)
        }
    }
}