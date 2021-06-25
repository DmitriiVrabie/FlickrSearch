package com.dvrabie.flickrsearch

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object FlickrClient {
    const val FLICKR_API_KEY = "d25b4f1f0bcf815fe9fe481a13bf700e"
    private const val CONNECTION_TIMEOUT_SECONDS = 15L
    private const val BASE_URL = "https://api.flickr.com/services/rest/"

    val client: FlickService by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
                )
            )
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(FlickService::class.java)

    }

}