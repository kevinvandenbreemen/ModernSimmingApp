package com.vandenbreemen.modernsimmingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import com.vandenbreemen.modernsimmingapp.R
import com.vandenbreemen.modernsimmingapp.databinding.FragmentSettingsBinding

class SettingsFragment: PreferenceFragmentCompat() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        view?.setBackgroundColor(resources.getColor(R.color.colorPrimary, activity?.theme))

        return view

    }

}