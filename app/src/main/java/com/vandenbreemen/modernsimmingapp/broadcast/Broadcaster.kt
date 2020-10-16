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

}