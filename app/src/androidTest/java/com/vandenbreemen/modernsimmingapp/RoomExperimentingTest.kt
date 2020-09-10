package com.vandenbreemen.modernsimmingapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vandenbreemen.modernsimmingapp.data.localstorage.Post
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomExperimentingTest {

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
        dao.storePost(post)
        assertEquals(1, dao.findPosts("content").size)
    }

}