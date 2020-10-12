package com.vandenbreemen.modernsimmingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor

class OverviewViewModel(private val simContentProviderInteractor: SimContentProviderInteractor): ViewModel() {

    private var mainMenuOpen = false

    private val selectedGroup: MutableLiveData<String> = MutableLiveData()
    val selectedGroupLiveData: LiveData<String> get() = selectedGroup

    private val groupList: MutableLiveData<List<String>> = MutableLiveData()
    val groupListLiveData: LiveData<List<String>> get() = groupList

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

    fun openGroupListMenu() {
        simContentProviderInteractor.fetchGroupNames{ names->
            groupList.postValue(names)
        }
    }



}