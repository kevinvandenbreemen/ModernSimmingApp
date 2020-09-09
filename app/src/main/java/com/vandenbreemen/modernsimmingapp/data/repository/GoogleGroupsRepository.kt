package com.vandenbreemen.modernsimmingapp.data.repository

import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsAPI
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsPost
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GooglePostContentLoader

/**
 *
 */
class GoogleGroupsRepository(private val api: GoogleGroupsAPI, private val contentLoader: GooglePostContentLoader) {

    fun getSims(groupName: String, num: Int): List<GoogleGroupsPost>? {
        val call = api.getRssFeed(groupName, num)
        val response = call.execute()
        if(response.isSuccessful) {
            return response.body()?.articleList
        }
        return null
    }

    /**
     * Get the actual content of the Google Groups post
     */
    fun getContent(post: GoogleGroupsPost): String? {
        post.link?.let {
            return contentLoader.getPostBody(it)
        }

        return null
    }

}