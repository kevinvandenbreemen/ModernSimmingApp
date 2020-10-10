package com.vandenbreemen.modernsimmingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vandenbreemen.modernsimmingapp.databinding.ActivityGroupSelectBinding
import com.vandenbreemen.modernsimmingapp.subscriber.SimContentProviderInteractor

private class GroupAdapter(private val groupNames: List<String>): RecyclerView.Adapter<GroupViewHolder>() {

    val selectionLiveData = MutableLiveData<String>()

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

class ActivityGroupSelect : AppCompatActivity() {

    companion object {
        const val SELECTED_GROUP = "___selectedGRP"
    }

    private lateinit var binding: ActivityGroupSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SimContentProviderInteractor(this.applicationContext).run {
            groupNamesLiveData.observe(this@ActivityGroupSelect, Observer { names->

                binding.groupSelectRecyclerView.apply {

                    layoutManager = LinearLayoutManager(this@ActivityGroupSelect)
                    setHasFixedSize(true)

                    val grpAdapter = GroupAdapter(names)
                    grpAdapter.selectionLiveData.observe(this@ActivityGroupSelect, Observer { selection->
                        val result = Intent()
                        result.putExtra(SELECTED_GROUP, selection)
                        setResult(RESULT_OK, result)
                        finish()
                    })
                    adapter = grpAdapter

                }

            })

            fetchGroupNames()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}