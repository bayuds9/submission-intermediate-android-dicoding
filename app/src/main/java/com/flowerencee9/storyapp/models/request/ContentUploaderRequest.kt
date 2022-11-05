package com.flowerencee9.storyapp.models.request

import java.io.File

data class ContentUploaderRequest(
    var latitude: Double? = null,
    var longitude: Double? = null,
    var imgFile: File? = null,
    var desc: String? = null
)
