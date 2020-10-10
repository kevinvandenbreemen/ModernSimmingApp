package com.vandenbreemen.modernsimmingapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.vandenbreemen.modernsimmingapp.activities.FunctionalityTestingActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(BuildConfig.showTestingTools) {
            findViewById<Button>(R.id.testFunctionality).visibility = VISIBLE
        }

    }

    fun testAppFunctionality(view: View) {
        startActivity(Intent(this, FunctionalityTestingActivity::class.java))
    }

}