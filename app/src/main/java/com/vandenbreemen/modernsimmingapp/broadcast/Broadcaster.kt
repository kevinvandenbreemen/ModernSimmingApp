package com.vandenbreemen.modernsimmingapp.broadcast

import android.content.Context
import android.content.Intent

class Broadcaster(private val context: Context) {

    companion object {
        const val NEW_POST_IN_GROUP = "NewPostInGroup"
        const val PARAM_GROUP_NAME = "groupName"
    }

    fun sendBroadcastForNewPostInGroup(groupName: String) {
        val intent = Intent("${context.applicationContext.packageName}:NewPosts")
        intent.putExtra(PARAM_GROUP_NAME, groupName)
        context.sendBroadcast(intent)
    }

}