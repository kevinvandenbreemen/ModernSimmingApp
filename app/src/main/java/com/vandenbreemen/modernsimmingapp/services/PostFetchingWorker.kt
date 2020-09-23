package com.vandenbreemen.modernsimmingapp.services

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vandenbreemen.modernsimmingapp.broadcast.Broadcaster
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.di.SimpleDI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostFetchingWorker(private val context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    private val interactor = SimpleDI().getFetchPostInteractor(context)
    private val postDatabase: PostsDatabase = SimpleDI().getPostsDatabase(context)
    private val broadcaster = Broadcaster(context)

    override fun doWork(): Result {

        Log.i(javaClass.canonicalName, "Executing Post Fetch Job....")

        CoroutineScope(Dispatchers.Default).launch {
            postDatabase.groupDao().list().forEach { group->
                Log.i(javaClass.canonicalName, "Fetching posts for '${group.name}'")
                withContext(Dispatchers.IO) {
                    if(interactor.fetch(group.name, 10)){
                        broadcaster.sendBroadcastForNewPostInGroup(group.name)
                    }
                }
            }
        }


        return Result.success()

    }
}