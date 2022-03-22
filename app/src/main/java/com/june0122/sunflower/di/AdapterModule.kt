package com.june0122.sunflower.di

import com.june0122.sunflower.ui.list.UserListAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {

    @Singleton
    @Provides
    fun provideAdapter(): UserListAdapter = UserListAdapter()
}