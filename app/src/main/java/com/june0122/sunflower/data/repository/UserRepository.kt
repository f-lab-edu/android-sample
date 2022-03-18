package com.june0122.sunflower.data.repository

import androidx.annotation.WorkerThread
import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.data.room.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getUsers()

    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @WorkerThread
    suspend fun delete(user: User) {
        userDao.delete(user)
    }
}
