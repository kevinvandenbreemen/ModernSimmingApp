package com.vandenbreemen.modernsimmingapp.fetcher

import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsPost
import com.vandenbreemen.modernsimmingapp.data.localstorage.Post
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostDao
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.data.repository.GoogleGroupsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class FetchPostInteractorTest {

    private lateinit var interactor: FetchPostInteractor

    private val mockGoogleGroupsRepository: GoogleGroupsRepository = mockk()
    private val mockPostDatabase: PostsDatabase = mockk()
    private val mockPostDao: PostDao = mockk(relaxed = true)

    @Before
    fun setup() {

        every { mockPostDatabase.postDao() } returns mockPostDao

        interactor = FetchPostInteractor(mockGoogleGroupsRepository, mockPostDatabase)
    }

    @Test
    fun `should fetch posts from Google Groups Repository`() {

        every { mockGoogleGroupsRepository.getSims("some-group", 10) } returns null

        interactor.fetch("some-group", 10)
        verify {
            mockGoogleGroupsRepository.getSims("some-group", 10)
        }

    }

    @Test
    fun `should convert the Google Groups posts to Posts for local storage`() {
        val googlePost = GoogleGroupsPost("Test Post", "https://www.example.com", "Kevin", "Thu, 10 Sep 2020 22:11:10 UTC")
        every { mockGoogleGroupsRepository.getSims("some-group", 10) } returns listOf(googlePost)
        every { mockGoogleGroupsRepository.getContent(googlePost) } returns "Test Content"

        //  1599775870000
        val expectedPost = Post(
            0,
            1599775870000,
            "Test Post",
            "Test Content"
        )

        interactor.fetch("some-group", 10)
        verify { mockPostDao.storePosts(listOf(expectedPost)) }
    }

}