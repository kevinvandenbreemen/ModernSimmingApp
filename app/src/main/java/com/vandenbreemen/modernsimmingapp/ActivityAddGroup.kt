package com.vandenbreemen.modernsimmingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.vandenbreemen.modernsimmingapp.databinding.LayoutAddGroupBinding
import com.vandenbreemen.modernsimmingapp.viewmodels.AddGroupViewModel
import com.vandenbreemen.modernsimmingapp.viewmodels.AddGroupViewModelProvider

class ActivityAddGroup : AppCompatActivity() {

    private lateinit var binding: LayoutAddGroupBinding

    private val viewModel: AddGroupViewModel by viewModels { AddGroupViewModelProvider(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutAddGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Now set up handlers etc
        viewModel.errorLiveData.observe(this, Observer { err->
            Toast.makeText(this, err, Toast.LENGTH_LONG).show()
        })

        viewModel.successLiveData.observe(this, Observer {
            Toast.makeText(this, resources.getString(R.string.add_group_success), Toast.LENGTH_LONG).show()
            finish()
        })

        binding.submitGroupName.setOnClickListener { _->
            viewModel.addGroup(binding.editTextTextPersonName.text.toString())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}