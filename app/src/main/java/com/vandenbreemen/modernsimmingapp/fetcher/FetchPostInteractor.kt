package com.vandenbreemen.modernsimmingapp.fetcher

import android.util.Log
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostBean
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.data.repository.GoogleGroupsRepository
import java.text.SimpleDateFormat
import java.util.*

/**
 * Core logic for fetching posts and storing them locally
 */
class FetchPostInteractor(private val googleGroupsRepository: GoogleGroupsRepository, private val database: PostsDatabase) {

    fun fetch(groupName: String, numPosts: Int): Boolean {

        val group = database.groupDao().findGroupByName(groupName)
            ?: throw RuntimeException("No group called $groupName exists in the app")
        val groupId = group.id

        val posts = googleGroupsRepository.getSims(groupName, numPosts)
        posts?.apply {

            val simpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)

            val postsToStore =
                filter { gp ->
                    gp.link?.let { postUrl ->
                        return@filter database.postDao().findPostByURL(postUrl).isEmpty()
                    }
                    false
                }.filter { gp -> gp.author != null && gp.pubDate != null && gp.title != null }
                    .mapNotNull { googlePost ->

                        googleGroupsRepository.getContent(googlePost)?.let { postContent ->
                            try {
                                simpleDateFormat.parse(googlePost.pubDate!!)?.let { date ->
                                    return@mapNotNull PostBean(
                                        date.time,
                                        googlePost.title!!,
                                        googlePost.link!!,
                                        groupId,
                                        postContent
                                    )
                                }

                            } catch (err: Exception) {
                                Log.e(
                                    javaClass.canonicalName,
                                    "Could not parse post date -- ${googlePost.pubDate}",
                                    err
                                )
                            }
                        }

                        null
                    }

            if (postsToStore.isEmpty()) {
                return false
            }

            (postsToStore as? List<PostBean>)?.let {
                database.postDao().storePosts(it)
            } ?: return false

            return true
        }

        return false
    }


}