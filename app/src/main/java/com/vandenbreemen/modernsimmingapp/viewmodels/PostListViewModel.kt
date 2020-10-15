package com.vandenbreemen.modernsimmingapp.viewmodels

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.vandenbreemen.modernsimmingapp.subscriber.PostView
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor
import com.vandenbreemen.modernsimmingcontentsubscriber.ModernSimmingBroadcasting

class PostListViewModel(private val simContentProviderInteractor: SimContentProviderInteractor, private val context: Context): ViewModel() {

    private val postsObserver: Observer<List<PostView>> = Observer { posts->
        postList.postValue(posts)
    }

    private val groupUpdatesReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if(!this@PostListViewModel::groupName.isInitialized) {
                return
            }

            Log.d(PostListViewModel::class.java.simpleName, "Fetching new list of posts for ${this@PostListViewModel.groupName}")
            intent.getStringExtra(ModernSimmingBroadcasting.PARAM_GROUP_NAME)?.let { groupName ->
                if(groupName == this@PostListViewModel.groupName) {
                    simContentProviderInteractor.fetchGroupPosts(groupName, 30)
                }
            }
        }

    }

    init {
        simContentProviderInteractor.postsLiveDate.observeForever(postsObserver)

        val newPostsFilter = IntentFilter("${context.applicationContext.packageName}:NewPosts")
        context.registerReceiver(groupUpdatesReceiver, newPostsFilter)
    }

    private val postList: MutableLiveData<List<PostView>> = MutableLiveData()
    val postListLiveData: LiveData<List<PostView>> get() = postList

    private lateinit var groupName: String

    fun updateGroupName(groupName: String) {
        this.groupName = groupName
        simContentProviderInteractor.fetchGroupPosts(this.groupName, 30)
    }

    override fun onCleared() {
        super.onCleared()

        simContentProviderInteractor.postsLiveDate.removeObserver(postsObserver)
        context.unregisterReceiver(groupUpdatesReceiver)
    }

}