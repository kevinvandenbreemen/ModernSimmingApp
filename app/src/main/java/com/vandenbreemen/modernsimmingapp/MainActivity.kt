package com.vandenbreemen.modernsimmingapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.vandenbreemen.modernsimmingapp.activities.FunctionalityTestingActivity
import com.vandenbreemen.modernsimmingapp.databinding.ActivityMainBinding
import com.vandenbreemen.modernsimmingapp.fragments.FragmentPlaybackBar
import com.vandenbreemen.modernsimmingapp.fragments.OnboardingFragment
import com.vandenbreemen.modernsimmingapp.fragments.SelectGroupDialogFragment
import com.vandenbreemen.modernsimmingapp.services.ServicesInteractor
import com.vandenbreemen.modernsimmingapp.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val ONBOARDING_DIALOG_ID = "__onboardingFrag"
        const val SELECT_GROUP_ID = "__selectGrpFrag"
    }

    private val onboardingViewModel: OnboardingViewModel by viewModels<OnboardingViewModel> { ModernSimmingViewModelFactory.fromActivity(this) }
    private val overviewViewModel: OverviewViewModel by viewModels<OverviewViewModel> { ModernSimmingViewModelFactory.fromActivity(this) }
    private val postListViewModel: PostListViewModel by viewModels<PostListViewModel> { ModernSimmingViewModelFactory.fromActivity(this) }
    private val playbackViewModel: PlaybackViewModel by viewModels<PlaybackViewModel> { ModernSimmingViewModelFactory.fromActivity(this) }

    @Inject lateinit var servicesInteractor: ServicesInteractor

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Set up view model stuff
        setupPostList()

        supportFragmentManager.beginTransaction().add(R.id.playbackContainer, FragmentPlaybackBar()).commit()

        setupOnboardingViewModel()

        setupOverviewViewModel()

        setupMenuBehaviour()

    }

    private fun setupPostList() {
        postListViewModel.selectedPostLiveData.observe(this, Observer { selected ->
            playbackViewModel.play(selected)
        })

        playbackViewModel.nextLiveData.observe(this, Observer {
            postListViewModel.gotoNextPost()
        })
        playbackViewModel.prevLiveData.observe(this, Observer {
            postListViewModel.gotoPrevPost()
        })
        playbackViewModel.stoppingLiveData.observe(this, Observer {
            postListViewModel.clearSelectedPost()
        })
    }

    private fun setupOverviewViewModel() {
        overviewViewModel.groupListMenuLiveData.observe(this, Observer { groupNames ->

            val fragment = SelectGroupDialogFragment(groupNames)
            fragment.selectedGroupLiveData.observe(this, Observer { groupName ->
                postListViewModel.updateGroupName(groupName)
            })
            fragment.show(supportFragmentManager, SELECT_GROUP_ID)

        })
    }

    private fun setupOnboardingViewModel() {
        onboardingViewModel.promptForGroupNameLiveData.observe(this, Observer {

            if (supportFragmentManager.findFragmentByTag(ONBOARDING_DIALOG_ID) != null) {
                return@Observer
            }

            val onboardingDialog = OnboardingFragment()
            onboardingDialog.show(supportFragmentManager, ONBOARDING_DIALOG_ID)
        })

        onboardingViewModel.groupNameAddedLiveData.observe(this, Observer {groupName ->
            servicesInteractor.ensurePostFetchRunning()

            postListViewModel.updateGroupName(groupName)

        })

        onboardingViewModel.onboardingNotNeededLiveData.observe(this, Observer {
            servicesInteractor.ensurePostFetchRunning()
        })
    }

    private fun setupMenuBehaviour() {


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