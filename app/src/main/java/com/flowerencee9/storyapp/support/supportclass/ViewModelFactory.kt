package com.flowerencee9.storyapp.support.supportclass

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flowerencee9.storyapp.networking.di.Injection
import com.flowerencee9.storyapp.screens.home.HomeViewModel
import com.flowerencee9.storyapp.screens.locations.MapsViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(Injection.provideStories(context)) as T
        }
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(Injection.provideStories(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}