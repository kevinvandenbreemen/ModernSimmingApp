package com.vandenbreemen.modernsimmingapp.config

import android.content.SharedPreferences

class SharedPreferencesInteractor(private val preferences: SharedPreferences) {

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

}