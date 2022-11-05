package com.flowerencee9.storyapp.screens.formuploader

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
class FormUploaderViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    @Mock
    private lateinit var vm: FormUploaderViewModel

    @Mock
    private lateinit var dummyMockFile: File

    @Test
    fun `verify uploadStory is works well by checking the return value of BasicResponse`() {
        val dumRequest = ContentUploaderRequest(44.1, 55.1, dummyMockFile, "test")
        val expect = MutableLiveData<BasicResponse>()
        expect.value = BasicResponse(false, "SUCCESS")

        vm.uploadContent(dumRequest)

        `when`(vm.basicResponse).thenReturn(expect)

        val actual = vm.basicResponse.getOrAwaitValue()
        verify(vm).basicResponse
        assertEquals(actual, expect.value)
    }
}