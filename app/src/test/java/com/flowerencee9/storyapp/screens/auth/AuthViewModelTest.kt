package com.flowerencee9.storyapp.screens.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.flowerencee9.storyapp.MainDispatcherRule
import com.flowerencee9.storyapp.getOrAwaitValue
import com.flowerencee9.storyapp.models.request.LoginRequest
import com.flowerencee9.storyapp.models.request.RegisterRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import com.flowerencee9.storyapp.models.response.LoginResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import kotlin.math.exp

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    @Mock
    private lateinit var vm: AuthViewModel

    @Test
    fun `verify userLogin works well by check the return value should be LoginResult`() {
        val dumRequest = LoginRequest("sukri@mail.to", "SigitAlSukri")
        val dumResult = LoginResult("sukri@mail.to", "tokensukri", "SigitAlSukri")

        val expect = MutableLiveData<LoginResult>()
        expect.value = dumResult

        vm.loginUser(dumRequest)
        verify(vm).loginUser(dumRequest)

        `when`(vm.loginData).thenReturn(expect)

        val actualData = vm.loginData.getOrAwaitValue()
        verify(vm).loginData
        assertEquals(actualData, expect.value)
    }

    @Test
    fun `verify userLogin works well by check the return value of BasicResponse`() {
        val dumRequest = LoginRequest("sukri@mail.to", "SigitAlSukri")
        val dumResult = BasicResponse(false, "SUCCESS")

        val expect = MutableLiveData<BasicResponse>()
        expect.value = dumResult

        vm.loginUser(dumRequest)
        verify(vm).loginUser(dumRequest)

        `when`(vm.basicResponse).thenReturn(expect)

        val actualData = vm.basicResponse.getOrAwaitValue()
        verify(vm).basicResponse
        assertEquals(actualData, expect.value)
    }

    @Test
    fun `verify userRegister works well by check the return value of BasicResponse`() {
        val dumRequest = RegisterRequest("sukri@mail.to", "Sigit Al Sukri", "SigitAlSukri")
        val dumResult = BasicResponse(false, "SUCCESS")

        val expect = MutableLiveData<BasicResponse>()
        expect.value = dumResult

        vm.registerUser(dumRequest)
        verify(vm).registerUser(dumRequest)

        `when`(vm.basicResponse).thenReturn(expect)

        val actualData = vm.basicResponse.getOrAwaitValue()
        verify(vm).basicResponse
        assertEquals(actualData, expect.value)
    }
}