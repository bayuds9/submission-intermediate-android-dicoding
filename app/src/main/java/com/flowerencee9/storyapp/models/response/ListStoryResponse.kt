package com.flowerencee9.storyapp.models.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class ListStoryResponse(
    @SerializedName("error")
    var error: Boolean = false,
    @SerializedName("listStory")
    var listStory: List<Story> = listOf(),
    @SerializedName("message")
    var message: String = ""
) : Parcelable