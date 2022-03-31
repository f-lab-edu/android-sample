package com.june0122.sunflower.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.june0122.sunflower.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT COUNT(*) FROM user_table WHERE name LIKE (:name)")
    suspend fun compareName(name: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table WHERE name LIKE (:name)")
    suspend fun delete(name: String)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}