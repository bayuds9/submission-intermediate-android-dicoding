package com.flowerencee9.storyapp.networking

import com.flowerencee9.storyapp.models.request.LoginRequest
import com.flowerencee9.storyapp.models.request.RegisterRequest
import com.flowerencee9.storyapp.models.response.BasicResponse
import com.flowerencee9.storyapp.models.response.ListStoryResponse
import com.flowerencee9.storyapp.models.response.LoginResponse
import com.flowerencee9.storyapp.models.response.Story
import com.flowerencee9.storyapp.support.Constants
import com.flowerencee9.storyapp.support.Constants.END_POINT.Companion.ADD_STORIES
import com.flowerencee9.storyapp.support.Constants.END_POINT.Companion.GET_LIST
import com.flowerencee9.storyapp.support.Constants.END_POINT.Companion.LOGIN
import com.flowerencee9.storyapp.support.Constants.END_POINT.Companion.REGISTER
import com.flowerencee9.storyapp.support.Constants.MULTIPART_FIELD.Companion.DESC
import com.flowerencee9.storyapp.support.Constants.MULTIPART_FIELD.Companion.LATITUDE
import com.flowerencee9.storyapp.support.Constants.MULTIPART_FIELD.Companion.LONGITUDE
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

class API {
    interface AUTH_SERVICE {
        @POST(LOGIN)
        fun loginUser(
            @Body loginRequest: LoginRequest
        ) : Call<LoginResponse>

        @POST(REGISTER)
        fun registerUser(
            @Body registerRequest: RegisterRequest
        ) : Call<BasicResponse>
    }

    interface CONTENT_SERVICE {
        @Multipart
        @POST(ADD_STORIES)
        fun addContent(
            @Part image: MultipartBody.Part,
            @Part(DESC) imgDesc: RequestBody,
            @Part(LATITUDE) latitude: Double?,
            @Part(LONGITUDE) longitude: Double?
        ) : Call<BasicResponse>

        @GET
        fun getListData(@Url url: String): Call<ListStoryResponse>

        @GET(GET_LIST)
        suspend fun getStories(
            @Query("page") page: Int,
            @Query("size") size: Int
        ): ListStoryResponse
    }
}