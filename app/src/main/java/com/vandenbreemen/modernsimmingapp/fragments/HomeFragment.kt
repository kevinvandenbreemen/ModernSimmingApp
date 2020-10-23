package com.vandenbreemen.modernsimmingapp.fragments

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.vandenbreemen.modernsimmingapp.BuildConfig
import com.vandenbreemen.modernsimmingapp.R
import com.vandenbreemen.modernsimmingapp.activities.FunctionalityTestingActivity
import com.vandenbreemen.modernsimmingapp.animation.OnAnimationEndListener
import com.vandenbreemen.modernsimmingapp.databinding.FragmentHomeBinding
import com.vandenbreemen.modernsimmingapp.viewmodels.ModernSimmingViewModelFactory
import com.vandenbreemen.modernsimmingapp.viewmodels.OverviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val overviewViewModel: OverviewViewModel by activityViewModels<OverviewViewModel> { ModernSimmingViewModelFactory.fromFragment(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        if(BuildConfig.showTestingTools) {
            binding.testFunctionality.visibility = View.VISIBLE
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPostList()

        childFragmentManager.beginTransaction().add(R.id.playbackContainer, FragmentPlaybackBar()).commit()

        setupMenuBehaviour()

    }

    private fun setupPostList() {
        val postListFragment = PostListFragment()
        childFragmentManager.beginTransaction().add(R.id.mainContentSection, postListFragment)
            .commit()
    }

    private fun setupMenuBehaviour() {

        binding.run {
            selectGroup.setOnClickListener { _->
                overviewViewModel.openGroupListMenu()
                overviewViewModel.closeMainMenu()
            }
        }

        overviewViewModel.openMenuLiveData.observe(this, Observer {

            binding.run {
                addGroupButton.alpha = 0.0f
                addGroupButton.visibility = View.VISIBLE
                addGroupButton.animate()
                    .alpha(1.0f)
                    .translationYBy(-20f)
                    .duration = 100

                selectGroup.apply {
                    alpha = 0.0f
                    visibility = View.VISIBLE
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
                    .setListener(object : OnAnimationEndListener() {
                        override fun onAnimationEnd(animation: Animator?) {
                            addGroupButton.run {
                                visibility = View.GONE
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
                                    visibility = View.GONE
                                    animate().setListener(null)
                                }
                            }
                        }).duration = 100
                }
            }
        })
    }

    fun testAppFunctionality(view: View) {
        startActivity(Intent(activity, FunctionalityTestingActivity::class.java))
    }

    fun toggleActionsMenu(view: View) {
        overviewViewModel.toggleMainMenu()
    }

}