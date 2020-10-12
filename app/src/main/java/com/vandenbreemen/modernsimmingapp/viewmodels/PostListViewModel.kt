package com.vandenbreemen.modernsimmingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.vandenbreemen.modernsimmingapp.subscriber.PostView
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor

class PostListViewModel(private val simContentProviderInteractor: SimContentProviderInteractor): ViewModel() {

    private val postsObserver: Observer<List<PostView>> = Observer { posts->
        postList.postValue(posts)
    }

    init {
        simContentProviderInteractor.postsLiveDate.observeForever(postsObserver)
    }

    private val postList: MutableLiveData<List<PostView>> = MutableLiveData()
    val postListLiveData: LiveData<List<PostView>> get() = postList

    private lateinit var groupName: String

    fun updateGroupName(groupName: String) {
        this.groupName = groupName
        simContentProviderInteractor.fetchGroupPosts(this.groupName, 30)
    }

}