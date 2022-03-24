package com.june0122.sunflower.di

import android.content.Context
import androidx.room.Room
import com.june0122.sunflower.data.api.GithubService
import com.june0122.sunflower.data.room.UserRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.github.com/"
    private val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
    private val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(logger)
    }.build()

    @Singleton
    @Provides
    fun provideGithubService(): GithubService {
        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(httpClient)
            addConverterFactory(GsonConverterFactory.create())
        }.build().create(GithubService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): UserRoomDatabase {
        return Room.databaseBuilder(
            context,
            UserRoomDatabase::class.java,
            "user_database"
        ).build()
    }
}