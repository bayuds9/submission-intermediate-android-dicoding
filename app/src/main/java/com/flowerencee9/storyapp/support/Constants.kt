package com.flowerencee9.storyapp.support

object Constants {
    interface END_POINT {
        companion object {
            const val REGISTER = "register"
            const val LOGIN = "login"
            const val GET_LIST = "stories?location=1"
            const val ADD_STORIES = "stories"
        }
    }

    interface MULTIPART_FIELD {
        companion object {
            const val _JSON = "json"
            const val _MULTIPART = "multipart/form-data"
            const val _BEARER = "Bearer"
            const val _PHOTO = "photo"
            const val DESC = "description"
            const val LATITUDE = "lat"
            const val LONGITUDE = "lon"
        }
    }
}