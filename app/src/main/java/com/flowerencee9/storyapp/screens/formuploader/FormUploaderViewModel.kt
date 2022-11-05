package com.flowerencee9.storyapp.screens.formuploader

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flowerencee9.storyapp.models.request.ContentUploaderRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import com.flowerencee9.storyapp.networking.Services
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormUploaderViewModel(application: Application) : AndroidViewModel(application) {
    private val service = Services(application.applicationContext).contentService
    private val TAG = FormUploaderViewModel::class.java.simpleName

    private val _basicResponse : MutableLiveData<BasicResponse> = MutableLiveData()
    val basicResponse : LiveData<BasicResponse> get() = _basicResponse

    private val _loadingStates : MutableLiveData<Boolean> = MutableLiveData()
    val loadingStates : LiveData<Boolean> get() = _loadingStates

    fun uploadContent(request: ContentUploaderRequest) {
        val uploaderCallback = object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                Log.d(TAG, "calling ${call.request()}")
                when(response.isSuccessful){
                    true -> response.body()?.let {
                        _basicResponse.value = BasicResponse(!response.isSuccessful, it.message)
                    }
                    else -> _basicResponse.value = BasicResponse(true, response.message())
                }
                _loadingStates.value = false
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d(TAG, "${t.message}")
                _basicResponse.value = BasicResponse(true, t.message.toString())
                _loadingStates.value = false
            }

        }
        val description = request.desc?.toRequestBody("text/plain".toMediaType())
        val pictureFile = request.imgFile?.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            request.imgFile?.name,
            pictureFile!!
        )
        _loadingStates.value = true
        service.addContent(
            imageMultipart,
            description!!,
            request.latitude,
            request.longitude
        ).enqueue(uploaderCallback)
    }
}