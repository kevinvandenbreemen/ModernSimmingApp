package com.vandenbreemen.modernsimmingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vandenbreemen.modernsimmingapp.R
import com.vandenbreemen.modernsimmingapp.databinding.LayoutSelectGroupBinding

private class GroupAdapter(private val groupNames: List<String>, private val selectionLiveData: MutableLiveData<String>): RecyclerView.Adapter<GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_group_item, parent, false))
    }

    override fun getItemCount(): Int {
        return groupNames.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val groupName = groupNames[position]
        (holder.itemView as? ViewGroup)?.apply {
            findViewById<TextView>(R.id.groupName)?.apply {
                text = groupName
            }

            setOnClickListener { _->
                selectionLiveData.postValue(groupName)
            }
        }
    }
}

class GroupViewHolder(view: View): RecyclerView.ViewHolder(view)

class SelectGroupDialogFragment(private val groupNames: List<String>): DialogFragment() {

    private lateinit var binding: LayoutSelectGroupBinding
    private lateinit var adapter: GroupAdapter

    private val selectedGroup = MutableLiveData<String>()
    val selectedGroupLiveData: LiveData<String> get() = selectedGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = LayoutSelectGroupBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.groupSelectRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GroupAdapter(groupNames, selectedGroup)
            setHasFixedSize(true)
        }

        selectedGroupLiveData.observe(viewLifecycleOwner, Observer { _->
            dialog?.dismiss()
        })

    }

}