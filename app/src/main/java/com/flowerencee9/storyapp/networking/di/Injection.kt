package com.flowerencee9.storyapp.networking.di

import android.content.Context
import com.flowerencee9.storyapp.models.data.StoryRepository
import com.flowerencee9.storyapp.models.database.StoryDatabase
import com.flowerencee9.storyapp.networking.Services

object Injection {
    fun provideStories(context: Context) : StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = Services(context).contentService
        return StoryRepository(database, apiService)
    }
}