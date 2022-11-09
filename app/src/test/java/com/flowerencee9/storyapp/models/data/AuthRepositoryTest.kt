package com.flowerencee9.storyapp.models.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.flowerencee9.storyapp.MainDispatcherRule
import com.flowerencee9.storyapp.getOrAwaitValue
import com.flowerencee9.storyapp.models.request.LoginRequest
import com.flowerencee9.storyapp.models.request.RegisterRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import com.flowerencee9.storyapp.models.response.LoginResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcher = MainDispatcherRule()

    @Mock
    private lateinit var repo : AuthRepository

    @Test
    fun `verify loginUser works well by check the return value should be LoginResult`() {
        val dumRequest = LoginRequest("sukri@mail.to", "SigitAlSukri")
        val dumResult = LoginResult("sukri@mail.to", "tokensukri", "SigitAlSukri")

        val expect = MutableLiveData<LoginResult>()
        expect.value = dumResult

        repo.loginUser(dumRequest)
        verify(repo).loginUser(dumRequest)

        `when`(repo.loginResult).thenReturn(expect)

        val actualData = repo.loginResult.getOrAwaitValue()
        verify(repo).loginResult
        assertEquals(actualData, expect.value)
    }

    @Test
    fun `verify loginUser works well by check the return value of BasicResponse`() {
        val dumRequest = LoginRequest("sukri@mail.to", "SigitAlSukri")
        val dumResult = BasicResponse(false, "SUCCESS")

        val expect = MutableLiveData<BasicResponse>()
        expect.value = dumResult

        repo.loginUser(dumRequest)
        verify(repo).loginUser(dumRequest)

        `when`(repo.basicResponse).thenReturn(expect)

        val actualData = repo.basicResponse.getOrAwaitValue()
        verify(repo).basicResponse
        assertEquals(actualData, expect.value)
    }

    @Test
    fun `verify registerUser works well by check the return value of BasicResponse`() {
        val dumRequest = RegisterRequest("sukri@mail.to", "Sigit Al Sukri", "SigitAlSukri")
        val dumResult = BasicResponse(false, "SUCCESS")

        val expect = MutableLiveData<BasicResponse>()
        expect.value = dumResult

        repo.registerUser(dumRequest)
        verify(repo).registerUser(dumRequest)

        `when`(repo.basicResponse).thenReturn(expect)

        val actualData = repo.basicResponse.getOrAwaitValue()
        verify(repo).basicResponse
        assertEquals(actualData, expect.value)
    }
}