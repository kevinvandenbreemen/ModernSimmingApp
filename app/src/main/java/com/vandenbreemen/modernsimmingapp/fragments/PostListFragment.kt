package com.vandenbreemen.modernsimmingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.vandenbreemen.modernsimmingapp.databinding.LayoutPostListBinding
import com.vandenbreemen.modernsimmingapp.uicommon.PostListRecyclerViewAdapter
import com.vandenbreemen.modernsimmingapp.viewmodels.ModernSimmingViewModelFactory
import com.vandenbreemen.modernsimmingapp.viewmodels.PostListViewModel

class PostListFragment: Fragment() {

    private val viewModel: PostListViewModel by activityViewModels<PostListViewModel> { ModernSimmingViewModelFactory.fromFragment(this) }
    private lateinit var viewBinding: LayoutPostListBinding
    private lateinit var adapter: PostListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = LayoutPostListBinding.inflate(inflater, container, false)

        adapter = PostListRecyclerViewAdapter()
        viewBinding.apply {
            postList.apply {
                adapter = this@PostListFragment.adapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.postListLiveData.observe(viewLifecycleOwner, Observer { postViews->
            adapter.setData(postViews)
        })

    }

}