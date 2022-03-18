package com.june0122.sunflower

import android.app.Application
import com.june0122.sunflower.data.repository.UserRepository
import com.june0122.sunflower.data.room.UserRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class UsersApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { UserRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { UserRepository(database.userDao()) }
}