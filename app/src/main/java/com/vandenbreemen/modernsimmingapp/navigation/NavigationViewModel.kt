package com.vandenbreemen.modernsimmingapp.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.vandenbreemen.modernsimmingapp.R
import com.vandenbreemen.modernsimmingapp.fragments.HomeFragmentDirections
import com.vandenbreemen.modernsimmingapp.fragments.SettingsFragmentDirections

/**
 * Handles updating the current nav button etc.
 */
class NavigationViewModel : ViewModel() {

    private val mutableHomeAsUpDrawable: MutableLiveData<Int> = MutableLiveData()
    val homeAsUpLiveData: LiveData<Int> get() = mutableHomeAsUpDrawable

    private lateinit var navController: NavController

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun update() {
        navController.currentDestination?.let { currentDestination ->
            when (currentDestination.id) {
                R.id.homeFragment -> mutableHomeAsUpDrawable.postValue(R.drawable.ic_hamburger)
                else -> mutableHomeAsUpDrawable.postValue(R.drawable.ic_back)
            }
        }
    }

    fun navigate() {

        navController.currentDestination?.let { currentDestination ->
            when(currentDestination.id) {
                R.id.homeFragment -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
                R.id.settingsFragment -> navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToHomeFragment())
            }
            update()
        }

    }

}