package com.flowerencee9.storyapp.models.response


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
@Entity(tableName = "story")
data class Story(
    @field:SerializedName("createdAt")
    var createdAt: String = "",

    @field:SerializedName("description")
    var description: String = "",

    @PrimaryKey
    @field:SerializedName("id")
    var id: String = "",

    @field:SerializedName("lat")
    var lat: Double? = null,

    @field:SerializedName("lon")
    var lon: Double? = null,

    @field:SerializedName("name")
    var name: String = "",

    @field:SerializedName("photoUrl")
    var photoUrl: String = ""
) : Parcelable