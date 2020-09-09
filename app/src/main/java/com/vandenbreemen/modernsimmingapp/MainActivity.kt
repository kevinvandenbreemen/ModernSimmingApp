package com.vandenbreemen.modernsimmingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.vandenbreemen.modernsimmingapp.data.googlegroups.GooglePostContentLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
}