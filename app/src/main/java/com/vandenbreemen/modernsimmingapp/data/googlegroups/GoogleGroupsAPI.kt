package com.vandenbreemen.modernsimmingapp.data.googlegroups

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleGroupsAPI {
    @GET("forum/feed/{groupName}/msgs/rss.xml")
    fun getRssFeed(
        @Path("groupName")
        groupName: String,
        @Query("num")
        postCount: Int): Call<GoogleGroupsRSSFeed>
}