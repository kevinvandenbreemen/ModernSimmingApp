package com.vandenbreemen.modernsimmingapp.uicommon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vandenbreemen.modernsimmingapp.databinding.LayoutPostItemBinding
import com.vandenbreemen.modernsimmingapp.subscriber.PostView

class PostListRecyclerViewAdapter(): RecyclerView.Adapter<PostListViewHolder>() {

    private var data: List<PostView> = emptyList()
    fun setData(data: List<PostView>) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListViewHolder {
        return PostListViewHolder(
            LayoutPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        val post = data[position]
        holder.binding.postTitle.text = post.title
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class PostListViewHolder(val binding: LayoutPostItemBinding): RecyclerView.ViewHolder(binding.root) {



}

