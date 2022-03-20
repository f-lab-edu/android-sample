package com.june0122.sunflower.data.api

import com.june0122.sunflower.data.entity.UserInfoDetail
import com.june0122.sunflower.data.entity.Users
import retrofit2.Call
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
    fun getUserDetail(@Path("username") userName: String): Call<UserInfoDetail>
}