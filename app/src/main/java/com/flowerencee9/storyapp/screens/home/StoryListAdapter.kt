package com.flowerencee9.storyapp.screens.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flowerencee9.storyapp.databinding.LayoutItemListBinding
import com.flowerencee9.storyapp.models.response.Story

class StoryListAdapter(
    private val listener: (Story) -> Unit
) : PagingDataAdapter<Story, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: LayoutItemListBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            with(binding){
                tvStoryName.text = data.name
                tvStoryDesc.text = data.description
                Glide.with(context).load(data.photoUrl).into(imgStory)
                root.setOnClickListener {
                    listener(data)
                }
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }
}