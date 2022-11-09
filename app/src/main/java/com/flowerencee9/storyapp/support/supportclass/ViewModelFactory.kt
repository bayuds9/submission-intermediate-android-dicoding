package com.flowerencee9.storyapp.support.supportclass

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flowerencee9.storyapp.networking.di.Injection
import com.flowerencee9.storyapp.screens.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(Injection.provideStories(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}