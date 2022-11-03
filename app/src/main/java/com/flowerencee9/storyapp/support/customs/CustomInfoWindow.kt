package com.flowerencee9.storyapp.support.customs

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.LayoutCustomMarkerBinding
import com.flowerencee9.storyapp.databinding.LayoutItemListBinding
import com.flowerencee9.storyapp.models.response.Story
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindow(private val context: Context) : GoogleMap.InfoWindowAdapter {
    val mWindow = (context as Activity).layoutInflater.inflate(R.layout.layout_custom_marker, null)

    private fun bindWindow(marker: Marker, view: View) {
        val binding = LayoutCustomMarkerBinding.bind(view)
        with(binding){
            title.text = marker.title
            snippet.text = marker.snippet
            Log.d("wkwk", "${marker.snippet}")
        }
    }

    override fun getInfoContents(p0: Marker): View? {
        bindWindow(p0, mWindow)
        return mWindow
    }

    override fun getInfoWindow(p0: Marker): View? {
        bindWindow(p0, mWindow)
        return mWindow
    }
}