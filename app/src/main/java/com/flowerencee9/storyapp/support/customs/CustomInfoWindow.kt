package com.flowerencee9.storyapp.support.customs

import android.app.Activity
import android.content.Context
import android.view.View
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.LayoutCustomMarkerBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindow(context: Context) : GoogleMap.InfoWindowAdapter {
    private val mWindow = (context as Activity).layoutInflater.inflate(R.layout.layout_custom_marker, null)

    private fun bindWindow(marker: Marker, view: View) {
        val binding = LayoutCustomMarkerBinding.bind(view)
        with(binding){
            title.text = marker.title
            snippet.text = marker.snippet
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