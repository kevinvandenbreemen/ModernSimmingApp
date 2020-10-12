package com.vandenbreemen.modernsimmingapp

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.vandenbreemen.modernsimmingapp.activities.FunctionalityTestingActivity
import com.vandenbreemen.modernsimmingapp.animation.OnAnimationEndListener
import com.vandenbreemen.modernsimmingapp.databinding.ActivityMainBinding
import com.vandenbreemen.modernsimmingapp.fragments.OnboardingFragment
import com.vandenbreemen.modernsimmingapp.services.ServicesInteractor
import com.vandenbreemen.modernsimmingapp.viewmodels.ModernSimmingViewModelFactory
import com.vandenbreemen.modernsimmingapp.viewmodels.OnboardingViewModel
import com.vandenbreemen.modernsimmingapp.viewmodels.OverviewViewModel
import com.vandenbreemen.modernsimmingapp.viewmodels.PostListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val ONBOARDING_DIALOG_ID = "__onboardingFrag"
    }

    private val onboardingViewModel: OnboardingViewModel by viewModels<OnboardingViewModel> { ModernSimmingViewModelFactory.fromActivity(this) }
    private val overviewViewModel: OverviewViewModel by viewModels<OverviewViewModel> { ModernSimmingViewModelFactory.fromActivity(this) }
    private val postListViewModel: PostListViewModel by viewModels<PostListViewModel> { ModernSimmingViewModelFactory.fromActivity(this) }

    @Inject lateinit var servicesInteractor: ServicesInteractor

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(BuildConfig.showTestingTools) {
            binding.testFunctionality.visibility = VISIBLE
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
            servicesInteractor.ensurePostFetchRunning()
        })

        onboardingViewModel.onboardingNotNeededLiveData.observe(this, Observer {
            servicesInteractor.ensurePostFetchRunning()
        })

        overviewViewModel.openMenuLiveData.observe(this, Observer {

            binding.run {
                addGroupButton.alpha = 0.0f
                addGroupButton.visibility = VISIBLE
                addGroupButton.animate()
                    .alpha(1.0f)
                    .translationYBy(-20f)
                    .duration = 100

                selectGroup.apply {
                    alpha = 0.0f
                    visibility = VISIBLE
                    animate()
                        .alpha(1.0f)
                        .translationYBy(-20f)
                        .duration = 100
                }
            }

        })

        overviewViewModel.closeMenuLiveData.observe(this, Observer {
            binding.run {
                addGroupButton.animate()
                    .alpha(0.0f)
                    .translationYBy(20f)
                    .setListener(object: OnAnimationEndListener() {
                        override fun onAnimationEnd(animation: Animator?) {
                            addGroupButton.run {
                                visibility = GONE
                                animate().setListener(null)
                            }
                        }
                }).duration = 100

                selectGroup.apply {
                    animate()
                        .alpha(0.0f)
                        .translationYBy(20f)
                        .setListener(object : OnAnimationEndListener() {
                            override fun onAnimationEnd(animation: Animator?) {
                                selectGroup.run {
                                    visibility = GONE
                                    animate().setListener(null)
                                }
                            }
                        }).duration = 100
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()

        onboardingViewModel.start()
    }

    fun testAppFunctionality(view: View) {
        startActivity(Intent(this, FunctionalityTestingActivity::class.java))
    }

    fun toggleActionsMenu(view: View) {
        overviewViewModel.toggleMainMenu()
    }

    fun addGroup(view: View) {}

}