package com.vandenbreemen.modernsimmingapp.data.localstorage

import android.util.Log
import androidx.room.*

@Dao
abstract class PostDao {

    @Insert
    @Transaction
    fun storePosts(posts: List<PostBean>) {
        posts.forEach {post->
            val content = post.content

            val postId = insertRawPost(Post(
                0, post.postedDate, post.title, post.url, post.groupId
            )).toInt()

            Log.d(javaClass.simpleName, "Storing PostContent (${postId.toInt()}, ($content))")

            val contentId = insertRawPostContent(PostContent(
                0, postId.toInt(), content
            )).toInt()

            updateRawPost(Post(postId, post.postedDate, post.title, post.url, post.groupId, contentId))
        }
    }

    @Insert
    abstract fun insertRawPost(post: Post): Long

    @Update
    abstract fun updateRawPost(post: Post)

    @Insert
    abstract fun insertRawPostContent(postContent: PostContent): Long

    @Query("SELECT * from Post WHERE contentId in (SELECT `rowid` from PostContent WHERE content MATCH :text)")
    abstract fun findPosts(text: String): List<Post>

    @Query("SELECT * from Post WHERE url=:url")
    abstract fun findPostByURL(url: String): List<Post>

    /**
     * Fetches raw posts (without content) for the group
     */
    @Query("SELECT * from Post WHERE groupId = :groupId order by post_date desc limit :maxPosts")
    abstract fun fetchRawPostsForGroup(groupId: Int, maxPosts: Int): List<Post>

    /**
     * Load the content of the post with the given id
     */
    @Query("SELECT content FROM PostContent where postId = :postId")
    abstract fun loadContent(postId: Int): String?

}