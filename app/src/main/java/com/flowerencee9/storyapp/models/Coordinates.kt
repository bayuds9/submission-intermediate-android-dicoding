package com.flowerencee9.storyapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coordinates(
    var lat: Double? = null,
    var long: Double? = null
) : Parcelable
