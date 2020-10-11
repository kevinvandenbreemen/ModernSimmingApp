package com.vandenbreemen.modernsimmingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor

class OnboardingViewModel(private val simContentProviderInteractor: SimContentProviderInteractor, private val addGroupViewModel: AddGroupViewModel): ViewModel() {

    private val promptForGroupName: MutableLiveData<Unit> = MutableLiveData()
    val promptForGroupNameLiveData: LiveData<Unit> get() = promptForGroupName

    private val groupNameAdded: MutableLiveData<Unit> = MutableLiveData()
    val groupNameAddedLiveData: LiveData<Unit> get() = groupNameAdded

    init {
        addGroupViewModel.successLiveData.observeForever {
            groupNameAdded.postValue(Unit)
        }
    }

    /**
     * Check to see if the user needs to be onboarded
     */
    fun start() {
        simContentProviderInteractor.fetchGroupNames{names->
            if(names.isEmpty()) {
                promptForGroupName.postValue(Unit)
            }
        }
    }

    fun addNewGroup(groupName: String) {
        addGroupViewModel.addGroup(groupName)
    }

}