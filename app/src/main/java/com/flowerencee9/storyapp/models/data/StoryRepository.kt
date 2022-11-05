package com.flowerencee9.storyapp.models.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.flowerencee9.storyapp.models.database.StoryDatabase
import com.flowerencee9.storyapp.models.response.Story
import com.flowerencee9.storyapp.networking.API

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: API.CONTENT_SERVICE) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory() : LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}