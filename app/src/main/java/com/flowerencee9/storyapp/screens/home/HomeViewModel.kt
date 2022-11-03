package com.flowerencee9.storyapp.screens.home

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flowerencee9.storyapp.models.data.StoryRepository
import com.flowerencee9.storyapp.models.response.ListStoryResponse
import com.flowerencee9.storyapp.models.response.Story
import com.flowerencee9.storyapp.networking.Services
import com.flowerencee9.storyapp.support.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    val stories: LiveData<PagingData<Story>> = storyRepository.getStory().cachedIn(viewModelScope)
}