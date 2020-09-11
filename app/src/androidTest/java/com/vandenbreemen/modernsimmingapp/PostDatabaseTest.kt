package com.vandenbreemen.modernsimmingapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vandenbreemen.modernsimmingapp.data.localstorage.Post
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostDatabaseTest {

    @Test
    fun testCreateAndSaveData() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, PostsDatabase::class.java).build()
        val dao = db.postDao()
        val post = Post(
            1,
            1123123143L,
            "test title",
            "test content"
        )
        dao.storePosts(listOf(post))
        assertEquals(1, dao.findPosts("content").size)
    }

    @Test
    fun testNoMatchForQuery() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, PostsDatabase::class.java).build()
        val dao = db.postDao()
        val post = Post(
            1,
            1123123143L,
            "test title",
            "test content"
        )
        dao.storePosts(listOf(post))
        assertTrue(dao.findPosts("no such post").isEmpty())
    }

    @Test
    fun learningTestHowToSetPrimaryKeyWhenInsertingMultipleRows() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, PostsDatabase::class.java).build()
        val dao = db.postDao()
        val post = Post(
            0,
            1123123143L,
            "test title",
            "test content"
        )
        dao.storePosts(listOf(post, post.copy()))

        val posts = dao.findPosts("content")
        assertEquals(2, posts.size)
        assertEquals(1, posts[0].id)
        assertEquals(2, posts[1].id)
    }


}