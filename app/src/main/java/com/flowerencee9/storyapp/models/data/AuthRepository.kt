package com.flowerencee9.storyapp.models.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flowerencee9.storyapp.models.request.LoginRequest
import com.flowerencee9.storyapp.models.request.RegisterRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import com.flowerencee9.storyapp.models.response.LoginResponse
import com.flowerencee9.storyapp.networking.API
import com.flowerencee9.storyapp.support.PREF
import com.flowerencee9.storyapp.support.SharedPref
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val service: API.AUTH_SERVICE
) {
    private val TAG = AuthRepository::class.java.simpleName
    private val sharedPref = SharedPref(context)

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
                    response.body()?.let {
                        _basicResponse.value = BasicResponse(false, it.message)
                    }
                    loginResponse?.let {
                        sharedPref.putString(PREF.BEARER_TOKEN, it.token)
                        sharedPref.putString(PREF.USER_NAME, it.name)
                        sharedPref.putString(PREF.USER_ID, it.userId)
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