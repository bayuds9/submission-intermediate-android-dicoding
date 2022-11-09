package com.flowerencee9.storyapp.screens.formuploader

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.flowerencee9.storyapp.getOrAwaitValue
import com.flowerencee9.storyapp.models.request.ContentUploaderRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FormUploaderViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var viewModel : FormUploaderViewModel

    @Mock
    private lateinit var dummyMockFile : File

    @Test
    fun `verify uploadStory on FormUploaderViewModel is works well by checking the return value of BasicResponse`() {
        val dumRequest = ContentUploaderRequest(44.1, 55.1, dummyMockFile, "test")
        val expect = MutableLiveData<BasicResponse>()
        expect.value = BasicResponse(false, "SUCCESS")

        viewModel.uploadContent(dumRequest)

        Mockito.`when`(viewModel.basicResponse).thenReturn(expect)

        val actual = viewModel.basicResponse.getOrAwaitValue()
        Mockito.verify(viewModel).basicResponse
        assertEquals(actual, expect.value)
    }
}