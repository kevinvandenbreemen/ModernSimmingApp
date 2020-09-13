package com.vandenbreemen.modernsimmingapp.subscriber

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 */
class SimContentProviderInteractor(private val context: Context) {

    companion object {
        const val AUTHORITY = "com.vandenbreemen.modernsimmingapp.SimContentProvider"
    }

    private val groupNames: MutableLiveData<List<String>> = MutableLiveData()
    val groupNamesLiveData: LiveData<List<String>> get() = groupNames


    fun fetchGroupNames() {
        val url = "content://$AUTHORITY/groups/"

        CoroutineScope(Dispatchers.IO).launch {
            context.contentResolver.query(Uri.parse(url), emptyArray(),null,null)?.let { cursor ->
                cursor.use { _ ->
                    val groupNames: MutableList<String> = mutableListOf()

                    if(cursor.moveToFirst()) {
                        do {
                            groupNames.add(cursor.getString(cursor.getColumnIndex("name")))
                        } while(cursor.moveToNext())
                    }

                    this@SimContentProviderInteractor.groupNames.postValue(groupNames)
                }
            }

        }
    }

}