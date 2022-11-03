package com.flowerencee9.storyapp.networking

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.flowerencee9.storyapp.support.getToken
import com.flowerencee9.storyapp.support.isLogin
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    private var retrofit: Retrofit? = null
    fun getClient(baseUrl: String, context: Context): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    if (context.isLogin()) builder.header(
                        "Authorization",
                        "Bearer ${context.getToken()}"
                    )
                    return@Interceptor chain.proceed(builder.build())
                }
            )
            addInterceptor(loggingInterceptor)
            addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
        }.build()

        if (retrofit == null) retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit!!
    }
}