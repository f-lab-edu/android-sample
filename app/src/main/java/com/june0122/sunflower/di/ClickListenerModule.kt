package com.june0122.sunflower.di

import com.june0122.sunflower.utils.UserClickListener
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class ClickListenerModule {

    @Binds
    abstract fun onUserClickListener(impl: UserClickListenerImpl): UserClickListener
}