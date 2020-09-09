package com.vandenbreemen.modernsimmingapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsAPI
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GooglePostContentLoader
import com.vandenbreemen.modernsimmingapp.data.repository.GoogleGroupsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

const val GOOGLE_GROUPS_BASE_URL = "https://groups.google.com/"

class MainActivity : AppCompatActivity() {

    lateinit var googleGroupsRepository: GoogleGroupsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val googleGroupsApi = Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build().create(
                GoogleGroupsAPI::class.java)
        googleGroupsRepository = GoogleGroupsRepository(googleGroupsApi)
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

            val sims = googleGroupsRepository.getSims("uss-odyssey-oe", 5)
            withContext(Main) {
                sims?.apply {
                    Toast.makeText(this@MainActivity, sims.toString(), Toast.LENGTH_LONG).show()
                } ?: run {
                    Toast.makeText(this@MainActivity, "Could not get sims!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}