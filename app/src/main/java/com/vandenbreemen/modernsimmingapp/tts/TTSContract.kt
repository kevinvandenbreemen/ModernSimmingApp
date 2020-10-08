package com.vandenbreemen.sim_assistant.mvp.tts

import com.vandenbreemen.modernsimmingapp.data.localstorage.PostBean
import com.vandenbreemen.util.SimplePublisher

interface TTSInteractor {

    val currentUtteranceSeekerPublisher: SimplePublisher<Pair<Int, Int>>

    fun speakPosts(sims: List<PostBean>)
    fun pause()
    fun resume()
    fun isPaused(): Boolean
    fun seekTo(position: Int)
    fun close()
    fun isCurrentlyInUse(): Boolean

}