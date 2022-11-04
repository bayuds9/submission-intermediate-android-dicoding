package com.flowerencee9.storyapp.models.data

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flowerencee9.storyapp.models.database.StoryDatabase
import com.flowerencee9.storyapp.models.response.BasicResponse
import com.flowerencee9.storyapp.models.response.ListStoryResponse
import com.flowerencee9.storyapp.models.response.Story
import com.flowerencee9.storyapp.networking.API
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okio.Timeout
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {
    private var mockAPI: API.CONTENT_SERVICE = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent(): Unit = runTest {
        val remoteMediator = StoryRemoteMediator(mockDb, mockAPI)
        val pagingState = PagingState<Int, Story>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
        Assert.assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : API.CONTENT_SERVICE {
    override fun addContent(
        image: MultipartBody.Part,
        imgDesc: RequestBody,
        latitude: Double?,
        longitude: Double?
    ): Call<BasicResponse> {
        val cb = object : Call<BasicResponse> {
            override fun clone(): Call<BasicResponse> {
                TODO("Not yet implemented")
            }

            override fun execute(): Response<BasicResponse> {
                TODO("Not yet implemented")
            }

            override fun enqueue(callback: Callback<BasicResponse>) {
                TODO("Not yet implemented")
            }

            override fun isExecuted(): Boolean {
                TODO("Not yet implemented")
            }

            override fun cancel() {
                TODO("Not yet implemented")
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun request(): Request {
                TODO("Not yet implemented")
            }

            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }

        }
        return cb
    }

    override suspend fun getStories(page: Int, size: Int): ListStoryResponse {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                description = "desc $i",
                id = "id $i",
                lat = i.toDouble(),
                lon = i.toDouble(),
                name = "name $i",
                photoUrl = "url/$i.jpg"
            )
            items.add(story)
        }
        return ListStoryResponse(
            error = false,
            listStory = items.subList((page - 1) * size, (page - 1) * size + size),
            message = "success"
        )
    }

}