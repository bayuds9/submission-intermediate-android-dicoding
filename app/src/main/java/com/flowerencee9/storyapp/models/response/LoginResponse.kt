package com.flowerencee9.storyapp.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("error")
    var error: Boolean = false,
    @SerializedName("loginResult")
    var loginResult: LoginResult = LoginResult(),
    @SerializedName("message")
    var message: String = ""
) : Parcelable