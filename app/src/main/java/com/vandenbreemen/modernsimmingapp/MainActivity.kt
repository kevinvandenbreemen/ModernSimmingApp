package com.vandenbreemen.modernsimmingapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsAPI
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsPost
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GooglePostContentLoader
import com.vandenbreemen.modernsimmingapp.data.repository.GoogleGroupsRepository
import com.vandenbreemen.modernsimmingapp.services.PostFetchingWorker
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

const val GOOGLE_GROUPS_BASE_URL = "https://groups.google.com/"

class MainActivity : AppCompatActivity() {

    companion object {
        const val GET_GROUP_NAME_TO_FETCH_POSTS_FOR = 111
    }

    private lateinit var googleGroupsRepository: GoogleGroupsRepository

    private val simContentProviderInteractor: SimContentProviderInteractor by lazy {
        applicationContext?.let { ctx ->
            return@lazy SimContentProviderInteractor(ctx)
        }
        throw RuntimeException("Missing app context")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val googleGroupsApi = Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build().create(
                GoogleGroupsAPI::class.java)
        googleGroupsRepository = GoogleGroupsRepository(googleGroupsApi, GooglePostContentLoader())

        simContentProviderInteractor.groupNamesLiveData.observe(this, Observer { groupNames->
            Toast.makeText(this@MainActivity, groupNames.toString(), Toast.LENGTH_LONG).show()
        })

        simContentProviderInteractor.postsLiveDate.observe(this, Observer { posts->
            Toast.makeText(this@MainActivity, posts.map { p->p.title }.toString(), Toast.LENGTH_LONG).show()
        })
    }

    fun testLoadingPost(view: View) {

        CoroutineScope(Dispatchers.IO).launch {
            val post = GoogleGroupsPost(link = "https://groups.google.com/d/msg/uss-odyssey-oe/jLebMNq2V9U/t3XY7i1FAgAJ")
            val content = googleGroupsRepository.getContent(post)
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

    fun doTestContentProvider(view: View) {
        simContentProviderInteractor.fetchGroupNames()
    }

    fun testWorkRequest(view: View) {

        val workRequest = PeriodicWorkRequest.Builder(PostFetchingWorker::class.java, 20, TimeUnit.MINUTES).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("${applicationContext.packageName}/${PostFetchingWorker::class.java.canonicalName}",
                ExistingPeriodicWorkPolicy.KEEP, workRequest
            )

    }

    fun addGroup(view: View) {
        startActivity(Intent(this, ActivityAddGroup::class.java))
    }

    fun testLoadingPosts(view: View) {
        val intent = Intent(this, ActivityGroupSelect::class.java)
        startActivityForResult(intent, GET_GROUP_NAME_TO_FETCH_POSTS_FOR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data == null) {
            return
        }

        when(requestCode) {
            GET_GROUP_NAME_TO_FETCH_POSTS_FOR -> {
                if(resultCode == Activity.RESULT_OK) {
                    data.getStringExtra(ActivityGroupSelect.SELECTED_GROUP)?.let { groupName->
                        simContentProviderInteractor.fetchGroupPosts(groupName, 2)
                    }
                }
            }
        }
    }
}