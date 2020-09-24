package com.vandenbreemen.modernsimmingapp.services

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vandenbreemen.modernsimmingapp.broadcast.Broadcaster
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.di.SimpleDI
import com.vandenbreemen.modernsimmingapp.di.hilt.BackendEntryPoint
import com.vandenbreemen.modernsimmingapp.fetcher.FetchPostInteractor
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostFetchingWorker(private val context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    lateinit var interactor: FetchPostInteractor

    private val postDatabase: PostsDatabase = SimpleDI().getPostsDatabase(context)
    private val broadcaster = Broadcaster(context)

    val backendEntryPoint: BackendEntryPoint get() = EntryPointAccessors.fromApplication(context.applicationContext, BackendEntryPoint::class.java)

    init {
        interactor = backendEntryPoint.getFetchPostsInteractor()
    }

    override fun doWork(): Result {

        Log.i(javaClass.canonicalName, "Executing Post Fetch Job....")

        CoroutineScope(Dispatchers.Default).launch {
            postDatabase.groupDao().list().forEach { group->
                Log.i(javaClass.canonicalName, "Fetching posts for '${group.name}'")
                withContext(Dispatchers.IO) {
                    if(interactor.fetch(group.name, 10)){
                        Log.i(javaClass.canonicalName, "New posts arrived.  Sending broadcast")
                        broadcaster.sendBroadcastForNewPostInGroup(group.name)
                    }
                }
            }
        }


        return Result.success()

    }
}