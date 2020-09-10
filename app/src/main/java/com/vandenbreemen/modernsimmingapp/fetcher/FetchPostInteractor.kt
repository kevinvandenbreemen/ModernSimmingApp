package com.vandenbreemen.modernsimmingapp.fetcher

import android.util.Log
import com.vandenbreemen.modernsimmingapp.data.localstorage.Post
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.data.repository.GoogleGroupsRepository
import java.text.SimpleDateFormat
import java.util.*

/**
 * Core logic for fetching posts and storing them locally
 */
class FetchPostInteractor(private val googleGroupsRepository: GoogleGroupsRepository, private val database: PostsDatabase) {

    fun fetch(groupName: String, numPosts: Int) {
        val posts = googleGroupsRepository.getSims(groupName, numPosts)
        posts?.apply {
            val simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
            filter { gp->gp.author != null && gp.pubDate != null && gp.title != null }.forEach { googlePost->

                try {
                    simpleDateFormat.parse(googlePost.pubDate!!).let { date->
                        val post = Post(
                            1, date.time, googlePost.title!!, googlePost.author!!
                        )
                        database.postDao().storePost(post)
                    }
                } catch(err: Exception) {
                    Log.e(javaClass.canonicalName, "Could not parse post date -- ${googlePost.pubDate}", err)
                }
            }
        }
    }


}