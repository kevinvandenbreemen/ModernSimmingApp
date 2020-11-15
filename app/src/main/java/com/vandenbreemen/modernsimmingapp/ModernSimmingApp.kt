package com.vandenbreemen.modernsimmingapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.atomic.AtomicBoolean

@HiltAndroidApp
class ModernSimmingApp: Application(), LifecycleObserver {

    companion object {
        const val NOTIFICATION_ID = "NotificationChannel.ModernSimmingApp"
    }

    private var appStopped: AtomicBoolean = AtomicBoolean(false)
    val isInBackground: Boolean get() = appStopped.get()

    override fun onCreate() {
        super.onCreate()
        setupNotificationChannel()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun setupNotificationChannel() {
        val notificationChannel = NotificationChannel(NOTIFICATION_ID, getString(R.string.notification_name), NotificationManager.IMPORTANCE_DEFAULT)
        (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.run {
            createNotificationChannel(notificationChannel)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackbround() {
        appStopped.set(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        appStopped.set(false)
    }
}