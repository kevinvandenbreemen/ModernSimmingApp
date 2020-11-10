package com.vandenbreemen.modernsimmingapp.broadcast

import android.content.Context
import android.content.Intent
import com.vandenbreemen.modernsimmingcontentsubscriber.ModernSimmingBroadcasting

class Broadcaster(private val context: Context) {

    companion object {
        const val NEW_POST_IN_GROUP = "NewPostInGroup"
        const val PARAM_GROUP_NAME = ModernSimmingBroadcasting.PARAM_GROUP_NAME
        const val PARAM_TTS_CURRENT_POSITION = ModernSimmingBroadcasting.PARAM_TTS_CURRENT_POSITION
        const val PARAM_TTS_TOTAL_STRINGS_TO_SPEAK = ModernSimmingBroadcasting.PARAM_TTS_TOTAL_STRINGS_TO_SPEAK
        const val TTS_SEEK_TO = ModernSimmingBroadcasting.TTS_SEEK_TO
        const val TTS_STOP = ModernSimmingBroadcasting.TTS_STOP
        const val TTS_PLAY_PAUSE = ModernSimmingBroadcasting.TTS_PLAY_PAUSE
        const val TTS_PAUSED = ModernSimmingBroadcasting.TTS_PAUSED
        const val TTS_PLAYING = ModernSimmingBroadcasting.TTS_PLAYING
        const val TTS_FINISHED = "TTS_FINISHED_PLAYBACK"

        /**
         * Indicates that the backend has started playing a post on its own (say after app restart)
         */
        const val TTS_START_AUTO_PLAY = "TTS_START_AUTO_PLAY"

        /**
         * ID of a post that an operation/action/etc broadcast concerns
         */
        const val TTS_POST_ID = "__TTS_POSTID"
    }

    fun sendBroadcastForNewContentInGroup(groupName: String) {
        val intent = Intent("${context.applicationContext.packageName}:NewPosts")
        intent.putExtra(PARAM_GROUP_NAME, groupName)
        context.sendBroadcast(intent)
    }

    fun sendBroadcastForCurrentTTSPosition(position: Int, totalStringsToSpeak: Int) {
        val intent = Intent("${context.applicationContext.packageName}:TTSSeekPosition")
        intent.putExtra(PARAM_TTS_CURRENT_POSITION, position)
        intent.putExtra(PARAM_TTS_TOTAL_STRINGS_TO_SPEAK, totalStringsToSpeak)
        context.sendBroadcast(intent)
    }

    fun sendBroadcastForPause() {
        val intent = Intent("${context.applicationContext.packageName}:$TTS_PAUSED")
        context.sendBroadcast(intent)
    }

    fun sendBroadcastForPlay() {
        val intent = Intent("${context.applicationContext.packageName}:$TTS_PLAYING")
        context.sendBroadcast(intent)
    }

    fun sendBroadcastForDonePlaying() {
        val intent = Intent("${context.applicationContext.packageName}:$TTS_FINISHED")
        context.sendBroadcast(intent)
    }

    fun sendBroadcastForAutoplayStart(postId: Int) {
        val intent = Intent("${context.applicationContext.packageName}:$TTS_START_AUTO_PLAY")
        intent.putExtra(TTS_POST_ID, postId)
        context.sendBroadcast(intent)
    }

}