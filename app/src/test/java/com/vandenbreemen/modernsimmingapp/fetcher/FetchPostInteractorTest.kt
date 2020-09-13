package com.vandenbreemen.modernsimmingapp.fetcher

import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsPost
import com.vandenbreemen.modernsimmingapp.data.localstorage.*
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
    private val mockGroupDao: GroupDao = mockk(relaxed = true)

    @Before
    fun setup() {

        every { mockPostDatabase.postDao() } returns mockPostDao
        every { mockPostDatabase.groupDao() } returns mockGroupDao

        interactor = FetchPostInteractor(mockGoogleGroupsRepository, mockPostDatabase)
    }

    @Test
    fun `should fetch posts from Google Groups Repository`() {

        every { mockGoogleGroupsRepository.getSims("some-group", 10) } returns null
        every { mockGroupDao.findGroupByName("some-group") } returns Group(1, "some-group")

        interactor.fetch("some-group", 10)
        verify {
            mockGoogleGroupsRepository.getSims("some-group", 10)
        }

    }

    @Test
    fun `should convert the Google Groups posts to Posts for local storage`() {
        every { mockGroupDao.findGroupByName("some-group") } returns Group(1, "some-group")
        val googlePost = GoogleGroupsPost("Test Post", "https://www.example.com", "Kevin", "Thu, 10 Sep 2020 22:11:10 UTC")
        every { mockGoogleGroupsRepository.getSims("some-group", 10) } returns listOf(googlePost)
        every { mockGoogleGroupsRepository.getContent(googlePost) } returns "Test Content"

        //  1599775870000
        val expectedPost = PostBean(
            1599775870000,
            "Test Post",
            "https://www.example.com",
            1,
            "Test Content"
        )

        every { mockPostDao.findPostByURL("https://www.example.com") } returns listOf()
        interactor.fetch("some-group", 10)
        verify { mockPostDao.storePosts(listOf(expectedPost)) }
    }

    @Test
    fun `should not store posts that have already been stored`() {
        every { mockGroupDao.findGroupByName("some-group") } returns Group(1, "some-group")
        val googlePost = GoogleGroupsPost("Test Post", "https://www.example.com", "Kevin", "Thu, 10 Sep 2020 22:11:10 UTC")
        every { mockGoogleGroupsRepository.getSims("some-group", 10) } returns listOf(googlePost)
        every { mockGoogleGroupsRepository.getContent(googlePost) } returns "Test Content"

        //  1599775870000

        val expectedPostToBeStored = PostBean(
            0,
            "Test Post",
            "http://www.example.com",
            1,
            "Test Content"
        )

        val expectedPostToBeFound = Post(
            0,
            1599775870000,
            "Test Post",
            "https://www.example.com",
            1
        )
        every { mockPostDao.findPostByURL("https://www.example.com") } returns listOf(expectedPostToBeFound)

        interactor.fetch("some-group", 10)

        verify(exactly = 0) { mockPostDao.storePosts(any()) }

    }

}