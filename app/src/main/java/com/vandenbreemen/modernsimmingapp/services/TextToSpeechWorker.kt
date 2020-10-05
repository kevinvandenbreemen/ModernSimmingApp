package com.vandenbreemen.modernsimmingapp.services

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostBean
import com.vandenbreemen.modernsimmingapp.di.hilt.BackendEntryPoint
import com.vandenbreemen.modernsimmingapp.fetcher.PostManagementInteractor
import com.vandenbreemen.sim_assistant.mvp.tts.TTSInteractor
import dagger.hilt.android.EntryPointAccessors

class TextToSpeechWorker(private val context: Context, private val args: WorkerParameters): Worker(context, args) {

    companion object {
        const val WORK_NAME = "__ttsNAME"
        const val KEY_POST_IDS = "__postIDS"
    }

    val backendEntryPoint: BackendEntryPoint get() = EntryPointAccessors.fromApplication(context.applicationContext, BackendEntryPoint::class.java)

    private val interactor: TTSInteractor = backendEntryPoint.getTTSInteractor()
    private val postManagementInterface: PostManagementInteractor = backendEntryPoint.getPostManagementInteractor()

    override fun doWork(): Result {

        val posts = mutableListOf<PostBean>()
        args.inputData.getIntArray(KEY_POST_IDS)?.let { postIds ->
            postIds.forEach {
                postManagementInterface.loadPost(it)?.let { bean->posts.add(bean) }
            }
        }

        interactor.speakSims(posts)
        return Result.success()

    }

    override fun onStopped() {
        super.onStopped()
        interactor.close()
    }
}