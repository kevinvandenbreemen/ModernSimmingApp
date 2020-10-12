package com.vandenbreemen.modernsimmingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OverviewViewModel: ViewModel() {

    private var mainMenuOpen = false

    private val openMenu: MutableLiveData<Unit> = MutableLiveData()
    val openMenuLiveData: LiveData<Unit> get() = openMenu

    private val closeMenu: MutableLiveData<Unit> = MutableLiveData()
    val closeMenuLiveData: LiveData<Unit> get() = closeMenu

    fun toggleMainMenu() {
        mainMenuOpen = !mainMenuOpen
        if(mainMenuOpen) {
            openMenu.postValue(Unit)
        } else {
            closeMenu.postValue(Unit)
        }
    }

}