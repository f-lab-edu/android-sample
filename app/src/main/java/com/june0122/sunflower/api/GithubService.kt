package com.june0122.sunflower.api

import com.june0122.sunflower.model.data.UserDetail
import com.june0122.sunflower.model.data.Users
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    fun getUserList(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): Call<Users>

    @GET("users/")
    fun getUserDetail(@Path("username") userName: String): Call<UserDetail>
}

private const val BASE_URL = "https://api.github.com/"

private val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

private val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(logger)
}.build()

private val retrofit = Retrofit.Builder().apply {
    baseUrl(BASE_URL)
    client(httpClient)
    addConverterFactory(GsonConverterFactory.create())
}.build()

val githubService: GithubService = retrofit.create(GithubService::class.java)