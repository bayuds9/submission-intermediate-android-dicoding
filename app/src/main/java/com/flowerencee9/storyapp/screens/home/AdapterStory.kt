package com.flowerencee9.storyapp.screens.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.LayoutItemListBinding
import com.flowerencee9.storyapp.models.response.Story
import com.flowerencee9.storyapp.support.supportclass.CallbackStory

class AdapterStory(
    private val context: Context,
    private val listener: (Story) -> Unit
) : RecyclerView.Adapter<AdapterStory.ViewHolder>() {
    private val listData = ArrayList<Story>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Story) = with(itemView) {
            val binding = LayoutItemListBinding.bind(itemView)
            binding.tvStoryName.text = item.name
            Glide.with(context).load(item.photoUrl).into(binding.imgStory)
            binding.root.setOnClickListener {
                listener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        (
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_item_list, parent, false)
                )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    fun setData(list: ArrayList<Story>) {
        val callbackDiff = CallbackStory(listData, list)
        val resultDiff = DiffUtil.calculateDiff(callbackDiff)
        listData.clear()
        listData.addAll(list)
        resultDiff.dispatchUpdatesTo(this)
    }
}