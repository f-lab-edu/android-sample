package com.june0122.sunflower.data.repository

import com.june0122.sunflower.data.entity.User
import com.june0122.sunflower.data.room.UserDao
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository @Inject constructor(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.flowUsers()

    suspend fun matcherName(name: String): Boolean = withContext(Dispatchers.IO) {
        userDao.matcherName(name) > 0
    }

    suspend fun insert(user: User) = withContext(Dispatchers.IO) {
        userDao.insert(user)
    }

    suspend fun delete(user: User) = withContext(Dispatchers.IO) {
        userDao.delete(user)
    }
}