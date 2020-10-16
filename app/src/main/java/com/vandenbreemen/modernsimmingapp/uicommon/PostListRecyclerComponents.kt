package com.vandenbreemen.modernsimmingapp.uicommon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vandenbreemen.modernsimmingapp.databinding.LayoutPostItemBinding
import com.vandenbreemen.modernsimmingapp.subscriber.PostView

class PostListRecyclerViewAdapter(private val itemSelectCallback: ((post: PostView) -> Unit)? = null): RecyclerView.Adapter<PostListViewHolder>() {

    private var data: List<PostView> = emptyList()
    fun setData(data: List<PostView>) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListViewHolder {
        return PostListViewHolder(
            LayoutPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onViewRecycled(holder: PostListViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.root.setOnClickListener(null)
    }

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        val post = data[position]
        holder.binding.postTitle.text = post.title
        itemSelectCallback?.let { callback->
            holder.binding.root.setOnClickListener {
                callback(post)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class PostListViewHolder(val binding: LayoutPostItemBinding): RecyclerView.ViewHolder(binding.root) {



}

