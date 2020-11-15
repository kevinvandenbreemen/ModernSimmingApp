package com.vandenbreemen.modernsimmingapp.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vandenbreemen.modernsimmingapp.MainActivity
import com.vandenbreemen.modernsimmingapp.ModernSimmingApp
import com.vandenbreemen.modernsimmingapp.R
import com.vandenbreemen.modernsimmingapp.broadcast.Broadcaster
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.di.hilt.BackendEntryPoint
import com.vandenbreemen.modernsimmingapp.fetcher.FetchPostInteractor
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostFetchingWorker(private val context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    lateinit var interactor: FetchPostInteractor

    lateinit var postDatabase: PostsDatabase

    lateinit var broadcaster: Broadcaster

    val backendEntryPoint: BackendEntryPoint get() = EntryPointAccessors.fromApplication(context.applicationContext, BackendEntryPoint::class.java)

    init {
        interactor = backendEntryPoint.getFetchPostsInteractor()
        postDatabase = backendEntryPoint.getPostsDatabase()
        broadcaster = backendEntryPoint.getBroadcaster()
    }

    override fun doWork(): Result {

        Log.i(javaClass.canonicalName, "Executing Post Fetch Job....")

        CoroutineScope(Dispatchers.Default).launch {
            postDatabase.groupDao().list().forEach { group->
                Log.i(javaClass.canonicalName, "Fetching posts for '${group.name}'")
                withContext(Dispatchers.IO) {
                    if(interactor.fetch(group.name, 10)){
                        Log.i(javaClass.canonicalName, "New posts arrived.  Sending broadcast")
                        broadcaster.sendBroadcastForNewContentInGroup(group.name)

                        (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.run {
                            val intent = Intent(context, MainActivity::class.java)
                            notify(42, NotificationCompat.Builder(context, ModernSimmingApp.NOTIFICATION_ID).run {
                                setContentTitle(context.getString(R.string.notification_title_new_posts, group.name))
                                setSmallIcon(R.drawable.ic_modernsimmingapp)
                                setContentIntent(PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT ))
                                build()
                            })
                        }


                    }
                }
            }
        }


        return Result.success()

    }
}