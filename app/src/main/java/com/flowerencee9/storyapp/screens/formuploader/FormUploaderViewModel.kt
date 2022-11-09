package com.flowerencee9.storyapp.screens.formuploader

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.flowerencee9.storyapp.models.data.ContentRepository
import com.flowerencee9.storyapp.models.data.StoryRepository
import com.flowerencee9.storyapp.models.request.ContentUploaderRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormUploaderViewModel @Inject constructor(
    private val repository: ContentRepository
) : ViewModel() {

    val basicResponse: LiveData<BasicResponse> get() = repository.basicResponse
    val loadingStates: LiveData<Boolean> get() = repository.loadingStates

    fun uploadContent(request: ContentUploaderRequest) = repository.uploadContent(request)
}