package com.vandenbreemen.modernsimmingapp.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.vandenbreemen.modernsimmingapp.databinding.FragmentOnboardingBinding
import com.vandenbreemen.modernsimmingapp.viewmodels.ModernSimmingViewModelFactory
import com.vandenbreemen.modernsimmingapp.viewmodels.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment: DialogFragment() {

    private val onboardingViewModel: OnboardingViewModel by activityViewModels<OnboardingViewModel> { ModernSimmingViewModelFactory.fromFragment(this) }

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOnboardingBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.apply {
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onboardingViewModel.groupNameAddedLiveData.observe(viewLifecycleOwner, Observer {
            dialog?.run {
                dismiss()
            }
        })

        binding.groupNameInputSection.submitGroupName.setOnClickListener{ _ ->
            val groupName = binding.groupNameInputSection.editTextTextPersonName.text.toString()
            onboardingViewModel.addNewGroup(groupName)
        }
    }


}