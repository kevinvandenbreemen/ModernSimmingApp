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

}