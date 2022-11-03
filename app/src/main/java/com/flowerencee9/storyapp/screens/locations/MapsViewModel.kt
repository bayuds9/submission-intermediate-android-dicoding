package com.flowerencee9.storyapp.screens.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flowerencee9.storyapp.models.data.StoryRepository
import com.flowerencee9.storyapp.models.response.Story

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    val stories: LiveData<PagingData<Story>> = storyRepository.getStory().cachedIn(viewModelScope)
}