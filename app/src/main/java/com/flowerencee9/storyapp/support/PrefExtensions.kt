package com.flowerencee9.storyapp.support

import android.content.Context
import android.content.SharedPreferences
import com.flowerencee9.storyapp.BuildConfig
import com.flowerencee9.storyapp.support.PREF.Companion.BEARER_TOKEN
import com.flowerencee9.storyapp.support.PREF.Companion.USER_ID
import com.flowerencee9.storyapp.support.PREF.Companion.USER_NAME

interface PREF {
    companion object {
        const val NAME = BuildConfig.APPLICATION_ID
        const val USER_ID = "USER_ID"
        const val USER_NAME = "USER_NAME"
        const val BEARER_TOKEN = "BEARER_TOKEN"
    }
}

class SharedPref(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF.NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    fun putString(key: String, value: String) {
        editor.putString(key, value)
            .apply()
    }

    fun clear() {
        editor.clear()
            .apply()
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, "").toString()
    }
}

fun Context.isLogin(): Boolean {
    val sharedPref = SharedPref(this)
    return sharedPref.getString(USER_ID).isNotEmpty()
}

fun Context.getUserName(): String {
    val sharedPref = SharedPref(this)
    return sharedPref.getString(USER_NAME)
}

fun Context.getToken(): String {
    val sharedPref = SharedPref(this)
    return sharedPref.getString(BEARER_TOKEN)
}

fun Context.removeUserPref() {
    val sharedPref = SharedPref(this)
    sharedPref.clear()
}