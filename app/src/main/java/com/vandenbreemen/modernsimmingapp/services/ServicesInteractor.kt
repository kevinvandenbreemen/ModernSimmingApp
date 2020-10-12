package com.vandenbreemen.modernsimmingapp.services

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Specialist for abstracting clients away work requests etc
 */
class ServicesInteractor(private val context: Context) {

    /**
     * Kicks off (if necessary) the post fetching process in the background
     */
    fun ensurePostFetchRunning() {
        val workRequest = PeriodicWorkRequest.Builder(PostFetchingWorker::class.java, 20, TimeUnit.MINUTES).build()

        WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork("${context.applicationContext.packageName}/${PostFetchingWorker::class.java.canonicalName}",
            ExistingPeriodicWorkPolicy.KEEP, workRequest
        )
    }

}