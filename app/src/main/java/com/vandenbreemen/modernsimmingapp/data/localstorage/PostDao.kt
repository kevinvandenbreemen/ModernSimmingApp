package com.vandenbreemen.modernsimmingapp.data.localstorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PostDao {

    @Insert
    fun storePost(post: Post)

    @Query("SELECT *, `rowid` from Post WHERE content MATCH :text")
    fun findPosts(text: String): List<Post>

}