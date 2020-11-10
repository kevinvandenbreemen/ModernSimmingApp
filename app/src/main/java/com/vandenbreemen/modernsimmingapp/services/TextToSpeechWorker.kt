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
import dagger.hilt.android.EntryPointAccessors
import java.lang.Thread.sleep

class TextToSpeechWorker(private val context: Context, private val args: WorkerParameters): Worker(context, args) {

    companion object {
        const val WORK_NAME = "__ttsNAME"
        const val KEY_POST_IDS = "__postIDS"
    }

    private var stopCallbacks: MutableList<()->Unit> = mutableListOf()

    override fun doWork(): Result {

        //  Setup
        val backendEntryPoint = EntryPointAccessors.fromApplication(context.applicationContext, BackendEntryPoint::class.java)
        val broadcaster = Broadcaster(context)
        val postManagementInteractor: PostManagementInteractor = backendEntryPoint.getPostManagementInteractor()
        val sharedPreferencesInteractor = backendEntryPoint.getSharedPreferencesInteractor()
        val configInteractor = backendEntryPoint.getConfigInteractor()

        if(configInteractor.isPlaybackStarted()) {
            if(sharedPreferencesInteractor.getBoolean("stop_playback_on_start", true)) {
                Log.d(javaClass.simpleName, "Speech was previously started.  Exiting")
                configInteractor.setPlaybackStarted(false)
                return Result.success()
            }

            //  Otherwise we need to signal the UI that we're going to be playing again!
            args.inputData.getIntArray(KEY_POST_IDS)?.let { postIds ->
                broadcaster.sendBroadcastForAutoplayStart(postIds[0])
            }
        }

        configInteractor.setPlaybackStarted(true)
        stopCallbacks.add {
            Log.d(javaClass.simpleName, "Switching previously started playback flag to off")
            configInteractor.setPlaybackStarted(false)
        }

        val interactor = backendEntryPoint.getTTSInteractor()
        stopCallbacks.add {
            interactor.close()
        }

        val handlerThread = HandlerThread("seek-handler")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        stopCallbacks.add { handlerThread.quitSafely() }

        interactor.currentUtteranceSeekerPublisher.subscribe { locationAndNumberOfUtterances->
            broadcaster.sendBroadcastForCurrentTTSPosition(locationAndNumberOfUtterances.first, locationAndNumberOfUtterances.second)
        }

        //  Receivers
        val seekReceiver = object: BroadcastReceiver() {
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
        stopCallbacks.add {
            context.unregisterReceiver(seekReceiver)
        }

        val filter = IntentFilter("${context.packageName}:${Broadcaster.TTS_SEEK_TO}")
        context.registerReceiver(seekReceiver, filter, null, handler)

        var paused = false
        val playPauseReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                paused = !paused
                if(paused){
                    interactor.pause()
                    broadcaster.sendBroadcastForPause()
                } else {
                    interactor.resume()
                    broadcaster.sendBroadcastForPlay()
                }
            }
        }
        context.registerReceiver(playPauseReceiver, IntentFilter("${context.packageName}:${Broadcaster.TTS_PLAY_PAUSE}"))
        stopCallbacks.add {
            context.unregisterReceiver(playPauseReceiver)
        }

        val stopReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                cleanup()
            }
        }
        context.registerReceiver(stopReceiver, IntentFilter("${context.applicationContext.packageName}:${Broadcaster.TTS_STOP}"))
        stopCallbacks.add {
            context.unregisterReceiver(stopReceiver)
        }

        //  Load/play the posts
        val posts = mutableListOf<PostBean>()
        args.inputData.getIntArray(KEY_POST_IDS)?.let { postIds ->
            postIds.forEach {
                postManagementInteractor.loadPost(it)?.let { bean->posts.add(bean) }
            }
        }

        interactor.speakPosts(posts)
        broadcaster.sendBroadcastForPlay()

        do {
            sleep(20)
        } while(interactor.isCurrentlyInUse())

        Log.d(javaClass.simpleName, "TTS processing complete.  Cleaning up")
        broadcaster.sendBroadcastForDonePlaying()
        cleanup()

        return Result.success()

    }

    override fun onStopped() {
        super.onStopped()
        Log.d(javaClass.simpleName, "work manager sent request for me to stop")

        cleanup()
    }

    @Synchronized
    private fun cleanup() {
        stopCallbacks.forEach { it() }
        stopCallbacks.clear()
    }
}