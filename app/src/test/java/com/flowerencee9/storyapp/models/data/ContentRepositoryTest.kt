package com.flowerencee9.storyapp.models.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.flowerencee9.storyapp.MainDispatcherRule
import com.flowerencee9.storyapp.getOrAwaitValue
import com.flowerencee9.storyapp.models.request.ContentUploaderRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ContentRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcher = MainDispatcherRule()

    @Mock
    private lateinit var repo : ContentRepository

    @Mock
    private lateinit var dummyMockFile : File

    @Test
    fun `verify uploadStory is works well by checking the return value of BasicResponse`() {
        val dumRequest = ContentUploaderRequest(44.1, 55.1, dummyMockFile, "test")
        val expect = MutableLiveData<BasicResponse>()
        expect.value = BasicResponse(false, "SUCCESS")

        repo.uploadContent(dumRequest)

        `when`(repo.basicResponse).thenReturn(expect)

        val actual = repo.basicResponse.getOrAwaitValue()
        verify(repo).basicResponse
        assertEquals(actual, expect.value)
    }
}