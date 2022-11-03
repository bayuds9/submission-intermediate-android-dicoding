package com.flowerencee9.storyapp.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BasicResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
) : Parcelable