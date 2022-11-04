package com.flowerencee9.storyapp.screens.locations

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.LayoutMapsItemListBinding
import com.flowerencee9.storyapp.models.response.Story
import com.flowerencee9.storyapp.support.supportclass.CallbackStory
import com.flowerencee9.storyapp.support.animateVisibility
import com.flowerencee9.storyapp.support.isViewShown
import com.flowerencee9.storyapp.support.toHide
import com.google.android.gms.maps.model.LatLng

class AdapterItemMapsData(
    private val locateListener : (LatLng) -> Unit,
    private val detailListener : (Story) -> Unit
) : PagingDataAdapter<Story, AdapterItemMapsData.ViewHolder>(DIFF_CALLBACK) {

    private val _snapshotData = MutableLiveData<List<Story>>()
    val snapshotData : LiveData<List<Story>>
    get() = _snapshotData

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    inner class ViewHolder(private val binding: LayoutMapsItemListBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Story) {
            with(binding){
                Glide.with(context).load(item.photoUrl).into(binding.imgStory)
                tvItemName.apply {
                    text = item.name
                    setOnClickListener {
                        animateHiddenAction(this@with)
                    }
                }
                imgStory.setOnClickListener {
                    animateHiddenAction(this)
                }
                tvActionLocate.setOnClickListener {
                    val itemLatLng = LatLng(item.lat!!, item.lon!!)
                    locateListener(itemLatLng)
                    containerItemAction.toHide()
                }
                tvActionDetail.setOnClickListener {
                    detailListener(item)
                    containerItemAction.toHide()
                }
            }
        }

    }

    private fun animateHiddenAction(binding: LayoutMapsItemListBinding) {
        val duration = 500
        with(binding){
            if (containerItemAction.isViewShown()) {
                containerItemAction.animateVisibility(false, duration.toLong())
            } else containerItemAction.animateVisibility(true, duration.toLong())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutMapsItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        val listData = snapshot().items
        if (listData.isNotEmpty()) _snapshotData.value = listData
        if (data != null) holder.bind(data)
    }



}