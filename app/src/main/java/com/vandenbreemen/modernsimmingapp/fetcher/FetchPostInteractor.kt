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

            val postsToStore = filter { gp->gp.author != null && gp.pubDate != null && gp.title != null }.mapNotNull { googlePost->

                googleGroupsRepository.getContent(googlePost) ?.let { postContent ->
                    try {
                        simpleDateFormat.parse(googlePost.pubDate!!)?.let { date->
                            return@mapNotNull Post(
                                0, date.time, googlePost.title!!, postContent
                            )
                        }

                    } catch(err: Exception) {
                        Log.e(javaClass.canonicalName, "Could not parse post date -- ${googlePost.pubDate}", err)
                    }
                }

                null
            }

            (postsToStore as? List<Post>)?.let {
                database.postDao().storePosts(it)
            }
        }
    }


}