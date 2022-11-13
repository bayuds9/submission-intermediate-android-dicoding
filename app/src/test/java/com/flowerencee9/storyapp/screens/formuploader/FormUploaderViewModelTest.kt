package com.flowerencee9.storyapp.screens.formuploader

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.flowerencee9.storyapp.getOrAwaitValue
import com.flowerencee9.storyapp.models.data.ContentRepository
import com.flowerencee9.storyapp.models.request.ContentUploaderRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FormUploaderViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var contentRepository: ContentRepository
    private lateinit var viewModel : FormUploaderViewModel

    @Mock
    private lateinit var dummyMockFile : File

    @Test
    fun `verify uploadStory on FormUploaderViewModel is works well for upload story with location by checking the return value of BasicResponse`() {
        val dumRequest = ContentUploaderRequest(44.1, 55.1, dummyMockFile, "test")
        val expect = MutableLiveData<BasicResponse>()
        expect.value = BasicResponse(false, "SUCCESS")

        contentRepository.uploadContent(dumRequest)
        `when`(contentRepository.basicResponse).thenReturn(expect)

        viewModel = FormUploaderViewModel(contentRepository)
        val actual = viewModel.basicResponse.getOrAwaitValue()
        assertEquals(actual, expect.value)
    }

    @Test
    fun `verify uploadStory on FormUploaderViewModel is works well for upload story without location by checking the return value of BasicResponse`() {
        val dumRequest = ContentUploaderRequest(null, null, dummyMockFile, "test")
        val expect = MutableLiveData<BasicResponse>()
        expect.value = BasicResponse(false, "SUCCESS")

        contentRepository.uploadContent(dumRequest)
        `when`(contentRepository.basicResponse).thenReturn(expect)

        viewModel = FormUploaderViewModel(contentRepository)
        val actual = viewModel.basicResponse.getOrAwaitValue()
        assertEquals(actual, expect.value)
    }
}