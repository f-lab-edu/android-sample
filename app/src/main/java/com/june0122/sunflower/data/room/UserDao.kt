package com.june0122.sunflower.data.room

import androidx.room.*
import com.june0122.sunflower.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun flowUsers(): Flow<List<User>>

    @Query("SELECT * FROM user_table")
    suspend fun getUsers(): List<User>

    @Query("SELECT COUNT(*) FROM user_table WHERE name LIKE (:name)")
    suspend fun matcherName(name: String): Int

    @Query("DELETE FROM user_table WHERE name LIKE (:name)")
    suspend fun remove(name: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}