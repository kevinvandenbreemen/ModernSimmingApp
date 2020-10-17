package com.vandenbreemen.modernsimmingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.vandenbreemen.modernsimmingapp.databinding.LayoutPlaybackBarBinding
import com.vandenbreemen.modernsimmingapp.viewmodels.ModernSimmingViewModelFactory
import com.vandenbreemen.modernsimmingapp.viewmodels.PlaybackViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentPlaybackBar: Fragment() {

    private val playbackViewModel: PlaybackViewModel by activityViewModels<PlaybackViewModel> { ModernSimmingViewModelFactory.fromFragment(this) }

    private lateinit var binding: LayoutPlaybackBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = LayoutPlaybackBarBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playbackViewModel.dictationPositionLiveData.observe(viewLifecycleOwner, Observer { position->
            binding.seekBar.max = position.second
            binding.seekBar.progress = position.first
        })

        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    playbackViewModel.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //  Un-needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //  Un-needed
            }
        })

        binding.playPause.apply {
            isEnabled = false
            isClickable = false
            setOnClickListener { playbackViewModel.playPause() }
        }

        playbackViewModel.playingLiveData.observe(viewLifecycleOwner, Observer {
            binding.playPause.apply {
                setImageResource(android.R.drawable.ic_media_pause)
                isEnabled = true
                isClickable = true
            }

        })
        playbackViewModel.pausingLiveData.observe(viewLifecycleOwner, Observer {
            binding.playPause.apply {
                setImageResource(android.R.drawable.ic_media_play)
                isEnabled = true
                isClickable = true
            }
        })
    }


}