package com.vandenbreemen.modernsimmingapp.viewmodels

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vandenbreemen.modernsimmingapp.data.localstorage.Group
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.di.SimpleDI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddGroupViewModel(private val postsDatabase: PostsDatabase): ViewModel() {

    private val mutableError = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = mutableError

    private val mutableSuccess = MutableLiveData<Unit>()
    val successLiveData: LiveData<Unit> get() = mutableSuccess

    fun addGroup(groupName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                tryAddGroup(groupName)
                mutableSuccess.postValue(Unit)
            } catch (exc: SQLiteConstraintException) {
                Log.e(javaClass.simpleName, "Could not add group since it already exists", exc)
                mutableError.postValue("Group '$groupName' already defined")
            }
        }

    }

    @Throws(SQLiteConstraintException::class)
    private fun tryAddGroup(groupName: String){
        postsDatabase.groupDao().store(Group(0, groupName))
    }

}

class AddGroupViewModelProvider(private val appContext: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(AddGroupViewModel::class.java.isAssignableFrom(modelClass)) {
            return AddGroupViewModel(SimpleDI().getPostsDatabase(appContext.applicationContext)) as T
        }

        throw RuntimeException("Unsupported type $modelClass")
    }
}