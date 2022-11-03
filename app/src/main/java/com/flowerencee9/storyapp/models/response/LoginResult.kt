package com.flowerencee9.storyapp.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("userId")
    var userId: String = ""
) : Parcelable