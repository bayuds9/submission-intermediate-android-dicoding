package com.flowerencee9.storyapp.screens.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.flowerencee9.storyapp.getOrAwaitValue
import com.flowerencee9.storyapp.models.request.LoginRequest
import com.flowerencee9.storyapp.models.request.RegisterRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var viewModel : AuthViewModel

    @Test
    fun `verify loginUser on AuthViewModel works well by check the return value of BasicResponse`() {
        val dumRequest = LoginRequest("sukri@mail.to", "SigitAlSukri")
        val dumResult = BasicResponse(false, "SUCCESS")

        val expect = MutableLiveData<BasicResponse>()
        expect.value = dumResult

        viewModel.loginUser(dumRequest)
        Mockito.verify(viewModel).loginUser(dumRequest)

        Mockito.`when`(viewModel.basicResponse).thenReturn(expect)

        val actualData = viewModel.basicResponse.getOrAwaitValue()
        Mockito.verify(viewModel).basicResponse
        assertEquals(actualData, expect.value)
    }

    @Test
    fun `verify registerUser on AuthViewModel works well by check the return value of BasicResponse`() {
        val dumRequest = RegisterRequest("sukri@mail.to", "Sigit Al Sukri", "SigitAlSukri")
        val dumResult = BasicResponse(false, "SUCCESS")

        val expect = MutableLiveData<BasicResponse>()
        expect.value = dumResult

        viewModel.registerUser(dumRequest)
        Mockito.verify(viewModel).registerUser(dumRequest)

        Mockito.`when`(viewModel.basicResponse).thenReturn(expect)

        val actualData = viewModel.basicResponse.getOrAwaitValue()
        Mockito.verify(viewModel).basicResponse
        assertEquals(actualData, expect.value)
    }
}