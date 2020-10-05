package com.vandenbreemen.modernsimmingapp.fetcher

import com.vandenbreemen.modernsimmingapp.data.localstorage.PostBean
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostDao

class PostManagementInteractor(private val postDao: PostDao) {

    fun loadPost(postId: Int): PostBean? {
        postDao.loadPost(postId)?.let{ post ->
            postDao.loadContent(postId)?.let { content->
                return PostBean(
                    post.postedDate,
                    post.title,
                    post.url ?: "",
                    post.groupId ?: 0,
                    content
                )
            }
        }

        return null
    }

}