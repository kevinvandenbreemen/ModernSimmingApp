package com.vandenbreemen.modernsimmingapp.data.localstorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PostDao {

    @Insert
    fun storePosts(posts: List<Post>)

    @Query("SELECT *, `rowid` from Post WHERE content MATCH :text")
    fun findPosts(text: String): List<Post>

    @Query("SELECT *, `rowid` from Post WHERE url=:url")
    fun findPostByURL(url: String): List<Post>

}