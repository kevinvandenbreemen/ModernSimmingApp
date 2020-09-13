package com.vandenbreemen.modernsimmingapp

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vandenbreemen.modernsimmingapp.data.localstorage.Group
import com.vandenbreemen.modernsimmingapp.data.localstorage.Post
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private lateinit var db: PostsDatabase

@RunWith(AndroidJUnit4::class)
class PostDatabaseTest {

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PostsDatabase::class.java).build()
    }



    @Test
    fun testCreateAndSaveData() {
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

    @Test
    fun testCanDetermineNonUniquePostBasedOnPostURL() {
        val dao = db.postDao()
        val post = Post(
            0,
            1123123143L,
            "test title",
            "test content",
            "http://www.example.com"
        )
        dao.storePosts(listOf(post))

        assertFalse(dao.findPostByURL("http://www.example.com").isEmpty())
    }

    @Test
    fun testStoresGroup() {
        val dao = db.groupDao()
        val group = Group(0, "test-group")
        dao.store(group)
        assertEquals(Group(1, "test-group"), dao.findGroupByName("test-group"))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun testCannotStoreSameGroupTwice() {
        val dao = db.groupDao()
        dao.store(Group(0, "test-group"))
        dao.store(Group(0, "test-group"))
    }

    @Test
    fun testLoadsAllGroups() {
        val dao = db.groupDao()
        dao.store(Group(0, "group1"))
        dao.store(Group(0, "group2"))

        val groups = dao.list()
        assertEquals(2, groups.size)
        assertEquals("group1", groups[0].name)
        assertEquals("group2", groups[1].name)
    }

    @Test
    fun testAssociatesPostWithGroup() {

        val dao = db.groupDao()
        dao.store(Group(0, "group1"))
        dao.store(Group(0, "group2"))

        val storedGroup = dao.findGroupByName("group1")

        val post = Post(
            0, 123, "test", "test content", "http://www.example.com", storedGroup!!.id
        )
        val postDao = db.postDao()
        postDao.storePosts(listOf(post))

        val postsForGroup = postDao.listPostsForGroup(storedGroup!!.id, 10)
        assertEquals(1, postsForGroup.size)
        assertEquals("test", postsForGroup[0].title)

        assertTrue(postDao.listPostsForGroup(2, 10).isEmpty())

    }

}