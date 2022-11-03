package com.flowerencee9.storyapp.models.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class ContentUploaderRequest(
    var latitude: Double? = null,
    var longitude: Double? = null,
    var multipartBody: MultipartBody.Part? = null,
    var requestBody: RequestBody? = null
)
