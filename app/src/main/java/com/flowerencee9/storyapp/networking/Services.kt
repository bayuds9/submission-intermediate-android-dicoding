package com.flowerencee9.storyapp.networking

import android.content.Context
import com.flowerencee9.storyapp.BuildConfig

class Services(private val context: Context) {
    val authService: API.AUTH_SERVICE
    get() = APIClient.getClient(BuildConfig.BASE_URL, context).create(API.AUTH_SERVICE::class.java)

    val contentService: API.CONTENT_SERVICE
    get() = APIClient.getClient(BuildConfig.BASE_URL, context).create(API.CONTENT_SERVICE::class.java)
}