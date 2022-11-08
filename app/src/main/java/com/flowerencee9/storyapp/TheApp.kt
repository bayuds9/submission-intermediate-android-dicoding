package com.flowerencee9.storyapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TheApp : Application() {
    companion object {
        var BASE_URL = BuildConfig.BASE_URL
    }
}