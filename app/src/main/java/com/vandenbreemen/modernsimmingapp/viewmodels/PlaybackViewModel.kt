package com.vandenbreemen.modernsimmingapp.viewmodels

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.vandenbreemen.modernsimmingapp.services.TextToSpeechWorker
import com.vandenbreemen.modernsimmingapp.subscriber.PostView

class PlaybackViewModel(private val context: Context): ViewModel() {

    private val dictationPositionReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val position = intent.getIntExtra("position", 0)
            val totalStrings = intent.getIntExtra("totalStringsToSpeak", 0)

            dictationPosition.postValue(Pair<Int,Int>(position, totalStrings))
        }

    }

    private val dictationPosition = MutableLiveData<Pair<Int, Int>>()

    /**
     * Pair with first member = current progress/position, second member = total number of strings to speak
     */
    val dictationPositionLiveData: LiveData<Pair<Int, Int>> get() = dictationPosition

    init {
        val intentFilter = IntentFilter("${context.applicationContext.packageName}:TTSSeekPosition")
        context.applicationContext.registerReceiver(dictationPositionReceiver, intentFilter)
    }

    /**
     * Play text to speech of the given post
     */
    fun play(postView: PostView) {
        val workRequest = OneTimeWorkRequestBuilder<TextToSpeechWorker>().setInputData(
            workDataOf(
                TextToSpeechWorker.KEY_POST_IDS to arrayOf(postView.id)
            )
        ).build()

        WorkManager.getInstance(context.applicationContext).run {
            enqueueUniqueWork(TextToSpeechWorker.WORK_NAME, ExistingWorkPolicy.REPLACE, workRequest)
        }
    }

    override fun onCleared() {
        super.onCleared()
        context.applicationContext.unregisterReceiver(dictationPositionReceiver)
    }

}