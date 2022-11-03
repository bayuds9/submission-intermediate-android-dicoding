package com.flowerencee9.storyapp.screens.formuploader

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.flowerencee9.storyapp.models.request.ContentUploaderRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import com.flowerencee9.storyapp.networking.Services
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormUploaderViewModel(application: Application) : AndroidViewModel(application) {
    private val service = Services(application.applicationContext).contentService
    private val TAG = FormUploaderViewModel::class.java.simpleName

    fun uploadContent(request: ContentUploaderRequest, respond: (BasicResponse) -> Unit) {
        val uploaderCallback = object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                Log.d(TAG, "calling ${call.request()}")
                when(response.isSuccessful){
                    true -> response.body()?.let(respond)
                    else -> respond(BasicResponse(response.body()?.error ?: true, response.body()?.message ?: response.message()))
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d(TAG, "${t.message}")
                respond(BasicResponse(true, t.message.toString()))
            }

        }
        Handler(Looper.getMainLooper()).post {
            service.addContent(
                request.multipartBody!!,
                request.requestBody!!,
                request.latitude,
                request.longitude
            ).enqueue(uploaderCallback)
        }
    }
}