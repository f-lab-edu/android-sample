package com.june0122.sunflower.data.repository

import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.data.room.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getUsers()

    suspend fun compareName(name: String): Boolean = withContext(Dispatchers.IO) {
        userDao.compareName(name) > 0
    }

    suspend fun insert(user: User) = withContext(Dispatchers.IO) {
        userDao.insert(user)
    }

    suspend fun delete(user: User) = withContext(Dispatchers.IO) {
        userDao.delete(user.name)
    }
}