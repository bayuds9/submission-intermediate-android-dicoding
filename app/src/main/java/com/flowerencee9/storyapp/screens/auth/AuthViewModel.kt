package com.flowerencee9.storyapp.screens.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flowerencee9.storyapp.models.request.LoginRequest
import com.flowerencee9.storyapp.models.request.RegisterRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import com.flowerencee9.storyapp.models.response.LoginResponse
import com.flowerencee9.storyapp.models.response.LoginResult
import com.flowerencee9.storyapp.networking.Services
import com.flowerencee9.storyapp.support.PREF
import com.flowerencee9.storyapp.support.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(context: Context) : ViewModel() {
    private val service = Services(context).authService
    private val TAG = AuthViewModel::class.java.simpleName
    private val sharedPref = SharedPref(context)

    private val _loginData: MutableLiveData<LoginResult> = MutableLiveData()
    val loginData: LiveData<LoginResult> get() = _loginData

    private val _basicResponse: MutableLiveData<BasicResponse> = MutableLiveData()
    val basicResponse: LiveData<BasicResponse> get() = _basicResponse

    private val _loadingStates: MutableLiveData<Boolean> = MutableLiveData()
    val loadingStates: LiveData<Boolean> get() = _loadingStates

    fun loginUser(request: LoginRequest) {
        val loginCallback = object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d(TAG, "calling ${call.request()}")
                if (response.isSuccessful) {
                    val loginResponse = response.body()?.loginResult
                    loginResponse?.let {
                        sharedPref.putString(PREF.BEARER_TOKEN, it.token)
                        sharedPref.putString(PREF.USER_NAME, it.name)
                        sharedPref.putString(PREF.USER_ID, it.userId)

                        _loginData.value = LoginResult(it.name, it.token, it.userId)
                    }
                    response.body()?.let {
                        _basicResponse.value = BasicResponse(false, it.message)
                    }
                } else _basicResponse.value = BasicResponse(true, response.message())
                _loadingStates.value = false
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d(TAG, "${t.message}")
                _basicResponse.value = BasicResponse(true, t.message.toString())
                _loadingStates.value = false
            }

        }
        _loadingStates.value = true
        service.loginUser(request).enqueue(loginCallback)
    }

    fun registerUser(request: RegisterRequest) {
        val registerCallback = object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                Log.d(TAG, "calling ${call.request()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        _basicResponse.value = BasicResponse(!response.isSuccessful, it.message)
                    }
                } else _basicResponse.value = BasicResponse(true, response.message())

                _loadingStates.value = false
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d(TAG, "${t.message}")
                _basicResponse.value = BasicResponse(true, t.message.toString())
                _loadingStates.value = false
            }

        }
        _loadingStates.value = true
        service.registerUser(request).enqueue(registerCallback)
    }

}