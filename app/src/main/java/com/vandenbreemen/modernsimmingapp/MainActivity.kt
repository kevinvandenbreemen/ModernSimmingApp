package com.vandenbreemen.modernsimmingapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.vandenbreemen.modernsimmingapp.activities.FunctionalityTestingActivity
import com.vandenbreemen.modernsimmingapp.fragments.OnboardingFragment
import com.vandenbreemen.modernsimmingapp.viewmodels.ModernSimmingViewModelFactory
import com.vandenbreemen.modernsimmingapp.viewmodels.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val ONBOARDING_DIALOG_ID = "__onboardingFrag"
    }

    private val onboardingViewModel: OnboardingViewModel by viewModels<OnboardingViewModel> { ModernSimmingViewModelFactory.fromActivity(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(BuildConfig.showTestingTools) {
            findViewById<Button>(R.id.testFunctionality).visibility = VISIBLE
        }

        //  Set up view model stuff
        onboardingViewModel.promptForGroupNameLiveData.observe(this, Observer {

            if(supportFragmentManager.findFragmentByTag(ONBOARDING_DIALOG_ID) != null) {
                return@Observer
            }

            val onboardingDialog = OnboardingFragment()
            onboardingDialog.show(supportFragmentManager, ONBOARDING_DIALOG_ID)
        })

        onboardingViewModel.groupNameAddedLiveData.observe(this, Observer {

        })

    }

    override fun onResume() {
        super.onResume()

        onboardingViewModel.start()
    }

    fun testAppFunctionality(view: View) {
        startActivity(Intent(this, FunctionalityTestingActivity::class.java))
    }

}