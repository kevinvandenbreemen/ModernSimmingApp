package com.vandenbreemen.modernsimmingapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vandenbreemen.modernsimmingapp.broadcast.Broadcaster
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostBean
import com.vandenbreemen.modernsimmingapp.di.hilt.BackendEntryPoint
import com.vandenbreemen.modernsimmingapp.fetcher.PostManagementInteractor
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import dagger.hilt.android.EntryPointAccessors
import java.lang.Thread.sleep

class TextToSpeechWorker(private val context: Context, private val args: WorkerParameters): Worker(context, args) {

    companion object {
        const val WORK_NAME = "__ttsNAME"
        const val KEY_POST_IDS = "__postIDS"
    }

    val backendEntryPoint: BackendEntryPoint get() = EntryPointAccessors.fromApplication(context.applicationContext, BackendEntryPoint::class.java)

    private val interactor: TTSInteractor = backendEntryPoint.getTTSInteractor()
    private val postManagementInterface: PostManagementInteractor = backendEntryPoint.getPostManagementInteractor()
    private val broadcaster = Broadcaster(context)

    private val seekReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val position = intent.getIntExtra(Broadcaster.PARAM_TTS_CURRENT_POSITION, -1)
            if(position < 0) {
                Log.e(javaClass.simpleName, "Missing position (${Broadcaster.PARAM_TTS_CURRENT_POSITION}) argument in broadcast to seek")
                return
            }
            Log.i(javaClass.simpleName, "Seeking to $position")
            interactor.seekTo(position)
        }

    }

    init {
        interactor.currentUtteranceSeekerPublisher.subscribe { locationAndNumberOfUtterances->
            broadcaster.sendBroadcastForCurrentTTSPosition(locationAndNumberOfUtterances.first, locationAndNumberOfUtterances.second)
        }

        val filter = IntentFilter("${context.packageName}:${Broadcaster.TTS_SEEK_TO}")
        Log.d("KEVIN", "Intent filter action=${context.packageName}:${Broadcaster.TTS_SEEK_TO}")

        val handlerThread = HandlerThread("seek-handler")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        context.registerReceiver(seekReceiver, filter, null, handler)
    }

    override fun doWork(): Result {

        val posts = mutableListOf<PostBean>()
        args.inputData.getIntArray(KEY_POST_IDS)?.let { postIds ->
            postIds.forEach {
                postManagementInterface.loadPost(it)?.let { bean->posts.add(bean) }
            }
        }

        interactor.speakPosts(posts)

        do {
            sleep(20)
        } while(interactor.isCurrentlyInUse())

        return Result.success()

    }

    override fun onStopped() {
        super.onStopped()
        interactor.close()

        context.unregisterReceiver(seekReceiver)
    }
}