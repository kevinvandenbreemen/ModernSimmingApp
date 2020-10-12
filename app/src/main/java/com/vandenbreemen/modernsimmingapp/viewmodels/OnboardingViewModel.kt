package com.vandenbreemen.modernsimmingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor

class OnboardingViewModel(private val simContentProviderInteractor: SimContentProviderInteractor, private val addGroupViewModel: AddGroupViewModel): ViewModel() {

    private val promptForGroupName: MutableLiveData<Unit> = MutableLiveData()
    val promptForGroupNameLiveData: LiveData<Unit> get() = promptForGroupName

    private val groupNameAdded: MutableLiveData<Unit> = MutableLiveData()
    val groupNameAddedLiveData: LiveData<Unit> get() = groupNameAdded

    private val onboardingNotNeeded: MutableLiveData<Unit> = MutableLiveData()
    val onboardingNotNeededLiveData: LiveData<Unit> get() = onboardingNotNeeded

    private val groupAddObserver = Observer<Unit> {
        groupNameAdded.postValue(Unit)
    }

    init {
        addGroupViewModel.successLiveData.observeForever(groupAddObserver)
    }

    /**
     * Check to see if the user needs to be onboarded
     */
    fun start() {
        simContentProviderInteractor.fetchGroupNames{names->
            if(names.isEmpty()) {
                promptForGroupName.postValue(Unit)
            } else {
                onboardingNotNeeded.postValue(Unit)
            }
        }
    }

    fun addNewGroup(groupName: String) {
        addGroupViewModel.addGroup(groupName)
    }

    override fun onCleared() {
        addGroupViewModel.successLiveData.removeObserver(groupAddObserver)
    }

}