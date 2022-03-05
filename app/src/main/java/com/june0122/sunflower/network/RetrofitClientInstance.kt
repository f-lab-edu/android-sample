package com.june0122.sunflower.network

import com.june0122.sunflower.service.GithubService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientInstance {
    private val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

    private val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(logger)
    }.build()

    private val retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        client(httpClient)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    val githubService: GithubService = retrofit.create(GithubService::class.java)

    companion object {
        private const val BASE_URL = "https://api.github.com/"
    }
}