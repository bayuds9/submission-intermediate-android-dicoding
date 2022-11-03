package com.flowerencee9.storyapp.screens.auth.login

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.flowerencee9.storyapp.models.request.LoginRequest
import com.flowerencee9.storyapp.models.response.LoginResponse
import com.flowerencee9.storyapp.networking.Services
import com.flowerencee9.storyapp.support.PREF
import com.flowerencee9.storyapp.support.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val service = Services(application.applicationContext).authService
    private val TAG = LoginViewModel::class.java.simpleName
    private val sharedPref = SharedPref(application.applicationContext)

    fun loginUser(request: LoginRequest, respond: (LoginResponse) -> Unit){
        val loginCallback = object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d(TAG, "calling ${call.request()}")
                when(response.isSuccessful){
                    true -> {
                        val loginResponse = response.body()?.loginResult
                        loginResponse?.let {
                            sharedPref.putString(PREF.BEARER_TOKEN, it.token)
                            sharedPref.putString(PREF.USER_NAME, it.name)
                            sharedPref.putString(PREF.USER_ID, it.userId)
                        }
                        response.body()?.let(respond)
                    }
                    else -> respond(LoginResponse(error = response.body()?.error ?: true, message = response.body()?.message ?: response.message()))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d(TAG, "${t.message}")
                respond(LoginResponse(true, message = t.message.toString()))
            }

        }
        Handler(Looper.getMainLooper()).post {
            service.loginUser(request).enqueue(loginCallback)
        }
    }
}