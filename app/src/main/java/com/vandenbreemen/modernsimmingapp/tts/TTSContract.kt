package com.vandenbreemen.sim_assistant.mvp.tts

import com.vandenbreemen.modernsimmingapp.data.localstorage.PostBean

interface TTSInteractor {
    fun speakSims(sims: List<PostBean>)
    fun pause()
    fun resume()
    fun isPaused(): Boolean
    fun seekTo(position: Int)
    fun close()
    fun isInProcessOfSpeakingSims(): Boolean

}