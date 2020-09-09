package com.vandenbreemen.modernsimmingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsAPI
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GooglePostContentLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

const val GOOGLE_GROUPS_BASE_URL = "https://groups.google.com/"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun testLoadingPost(view: View) {

        CoroutineScope(Dispatchers.IO).launch {
            val content = GooglePostContentLoader().getPostBody("https://groups.google.com/d/msg/uss-odyssey-oe/jLebMNq2V9U/t3XY7i1FAgAJ")
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, content, Toast.LENGTH_LONG).show()
            }
        }

    }

    fun testLoadingGroup(view: View) {
        CoroutineScope(Dispatchers.IO).launch {

            val googleGroupsApi = Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create()).build().create(
                    GoogleGroupsAPI::class.java)


            val list = googleGroupsApi.getRssFeed("uss-odyssey-oe", 5).execute().body()?.articleList?.map { art->art.title }
            list?.let { posts->
                withContext(Main) {
                    Toast.makeText(this@MainActivity, posts.toString(), Toast.LENGTH_LONG).show()
                }
            }

        }
    }
}