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
import com.vandenbreemen.modernsimmingapp.config.ConfigInteractor
import com.vandenbreemen.modernsimmingapp.subscriber.PostView
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor
import com.vandenbreemen.modernsimmingcontentsubscriber.ModernSimmingBroadcasting

class PostListViewModel(private val simContentProviderInteractor: SimContentProviderInteractor,
                        private val configInteractor: ConfigInteractor,
                        private val context: Context): ViewModel() {

    private var rawPostsList: List<PostView>? = null

    private val postsObserver: Observer<List<PostView>> = Observer { posts->
        rawPostsList = posts
        postList.postValue(rawPostsList)
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

        configInteractor.getSelectedGroup()?.let { selectedGroup ->
            doUpdateGroupName(selectedGroup)
        }
    }

    private val postList: MutableLiveData<List<PostView>> = MutableLiveData()
    val postListLiveData: LiveData<List<PostView>> get() = postList

    private val selectedPost: MutableLiveData<PostView> = MutableLiveData()
    val selectedPostLiveData: LiveData<PostView> get() = selectedPost

    private val selectedIndex: MutableLiveData<Int> = MutableLiveData()
    val selectedIndexLiveData: LiveData<Int> get() = selectedIndex

    private lateinit var groupName: String

    fun updateGroupName(groupName: String) {
        configInteractor.setSelectedGroup(groupName)
        doUpdateGroupName(groupName)
    }

    private fun doUpdateGroupName(groupName: String) {
        this.groupName = groupName
        simContentProviderInteractor.fetchGroupPosts(this.groupName, 30)
    }

    fun selectPost(postView: PostView) {

        //  Deselect current post
        rawPostsList?.apply {
            indexOfFirst { it.selected }.let {
                if(it >= 0) {
                    this[it].selected = false
                }
            }
        }

        postView.selected = true
        selectedPost.postValue(postView)
        rawPostsList?.indexOf(postView).apply {
            selectedIndex.postValue(this)
        }
    }

    /**
     *
     */
    fun gotoNextPost() {
        rawPostsList?.apply {
            var selectedIndex = indexOfFirst { it.selected }

            if(selectedIndex < 0) {
                return
            }

            get(selectedIndex).selected = false

            selectedIndex ++
            selectedIndex %= size

            get(selectedIndex).apply {
                selected = true
                selectPost(this)
            }

        }
    }

    fun gotoPrevPost() {
        rawPostsList?.apply {
            var selectedIndex = indexOfFirst { it.selected }

            if(selectedIndex < 0) {
                return
            }

            get(selectedIndex).selected = false

            selectedIndex --
            if(selectedIndex < 0) {
                selectedIndex = size-1
            }

            get(selectedIndex).apply {
                selected = true
                selectPost(this)
            }

        }
    }

    override fun onCleared() {
        super.onCleared()

        simContentProviderInteractor.postsLiveDate.removeObserver(postsObserver)
        context.unregisterReceiver(groupUpdatesReceiver)
    }

}