package com.flowerencee9.storyapp.networking.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.flowerencee9.storyapp.TheApp.Companion.BASE_URL
import com.flowerencee9.storyapp.models.data.StoryRepository
import com.flowerencee9.storyapp.models.database.StoryDatabase
import com.flowerencee9.storyapp.networking.API
import com.flowerencee9.storyapp.networking.Services
import com.flowerencee9.storyapp.support.getToken
import com.flowerencee9.storyapp.support.isLogin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object Injection {
    fun provideStories(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = Services(context).contentService
        return StoryRepository(database, apiService)
    }

    @Provides
    fun provideClient(@ApplicationContext context: Context): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

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

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun getAuthService(retrofit: Retrofit): API.AUTH_SERVICE {
        val api: API.AUTH_SERVICE by lazy { retrofit.create(API.AUTH_SERVICE::class.java) }
        return api
    }

    @Provides
    fun getContentService(retrofit: Retrofit): API.CONTENT_SERVICE {
        val api: API.CONTENT_SERVICE by lazy { retrofit.create(API.CONTENT_SERVICE::class.java) }
        return api
    }

}