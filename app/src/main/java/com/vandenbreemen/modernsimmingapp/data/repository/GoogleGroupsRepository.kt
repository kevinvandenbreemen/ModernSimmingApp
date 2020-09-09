package com.vandenbreemen.modernsimmingapp.data.repository

import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsAPI
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsPost

/**
 *
 */
class GoogleGroupsRepository(private val api: GoogleGroupsAPI) {

    fun getSims(groupName: String, num: Int): List<GoogleGroupsPost>? {
        val call = api.getRssFeed(groupName, num)
        val response = call.execute()
        if(response.isSuccessful) {
            return response.body()?.articleList
        }
        return null
    }

}