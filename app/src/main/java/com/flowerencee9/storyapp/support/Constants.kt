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
            const val DESC = "description"
            const val LATITUDE = "lat"
            const val LONGITUDE = "lon"
        }
    }
}