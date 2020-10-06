package com.vandenbreemen.modernsimmingapp.broadcast

import android.content.Context
import android.content.Intent

class Broadcaster(private val context: Context) {

    companion object {
        const val NEW_POST_IN_GROUP = "NewPostInGroup"
        const val PARAM_GROUP_NAME = "groupName"
        const val PARAM_TTS_CURRENT_POSITION = "position"
        const val PARAM_TTS_TOTAL_STRINGS_TO_SPEAK = "totalStringsToSpeak"
    }

    fun sendBroadcastForNewPostInGroup(groupName: String) {
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