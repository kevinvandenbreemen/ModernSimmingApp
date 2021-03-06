package com.vandenbreemen.modernsimmingapp.config

import android.content.Context

/**
 * Configuration for the entire app
 */
class ConfigInteractor(private val context: Context) {

    private val sharedPreferences = context.applicationContext.getSharedPreferences("ModernSimmingApp", Context.MODE_PRIVATE)

    fun setSelectedGroup(groupName: String) {
        sharedPreferences.edit().putString("selectedGroup", groupName).apply()
    }

    fun getSelectedGroup(): String? {
        return sharedPreferences.getString("selectedGroup", null)
    }

    fun setPlaybackStarted(started: Boolean) {
        sharedPreferences.edit().putBoolean("playbackStarted", started).apply()
    }

    fun isPlaybackStarted(): Boolean {
        return sharedPreferences.getBoolean("playbackStarted", false)
    }

    fun setSelectedPostId(id: Int) {
        sharedPreferences.edit().putInt("selectedPostId", id).apply()
    }

    fun getSelectedPostId(): Int? {
        val ret = sharedPreferences.getInt("selectedPostId", -1)
        return if(ret > 0) { ret } else null
    }

}