package com.vandenbreemen.modernsimmingapp

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GoogleGroupsPost
import com.vandenbreemen.modernsimmingapp.data.repository.GoogleGroupsRepository
import com.vandenbreemen.modernsimmingapp.services.PostFetchingWorker
import com.vandenbreemen.modernsimmingapp.services.TextToSpeechWorker
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val GET_GROUP_NAME_TO_FETCH_POSTS_FOR = 111
    }

    @Inject
    lateinit var googleGroupsRepository: GoogleGroupsRepository

    private val simContentProviderInteractor: SimContentProviderInteractor by lazy {
        applicationContext?.let { ctx ->
            return@lazy SimContentProviderInteractor(ctx)
        }
        throw RuntimeException("Missing app context")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simContentProviderInteractor.groupNamesLiveData.observe(this, Observer { groupNames->
            Toast.makeText(this@MainActivity, groupNames.toString(), Toast.LENGTH_LONG).show()
        })

        simContentProviderInteractor.postsLiveDate.observe(this, Observer { posts->
            Toast.makeText(this@MainActivity, posts.map { p->p.title }.toString(), Toast.LENGTH_LONG).show()
        })

        setupSeekbarReceiver()
    }

    private fun setupSeekbarReceiver() {
        val intentFilter = IntentFilter("${packageName}:TTSSeekPosition")
        val receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val position = intent.getIntExtra("position", 0)
                val totalStrings = intent.getIntExtra("totalStringsToSpeak", 0)

                seekBar.max = totalStrings
                seekBar.progress = position
            }

        }

        registerReceiver(receiver, intentFilter)
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

    fun doTryDictation(view: View) {

        val postId = 1
        val workRequest = OneTimeWorkRequestBuilder<TextToSpeechWorker>().setInputData(
            workDataOf(
                TextToSpeechWorker.KEY_POST_IDS to arrayOf(postId)
            )
        ).build()

        WorkManager.getInstance(this).enqueueUniqueWork(TextToSpeechWorker.WORK_NAME, ExistingWorkPolicy.REPLACE, workRequest)
    }
}