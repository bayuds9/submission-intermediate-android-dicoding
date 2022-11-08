package com.flowerencee9.storyapp.screens.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.flowerencee9.storyapp.models.data.AuthRepository
import com.flowerencee9.storyapp.models.request.LoginRequest
import com.flowerencee9.storyapp.models.request.RegisterRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    val basicResponse: LiveData<BasicResponse> get() = repository.basicResponse
    val loadingStates: LiveData<Boolean> get() = repository.loadingStates

    fun loginUser(loginRequest: LoginRequest) = repository.loginUser(loginRequest)

    fun registerUser(registerRequest: RegisterRequest) = repository.registerUser(registerRequest)

}