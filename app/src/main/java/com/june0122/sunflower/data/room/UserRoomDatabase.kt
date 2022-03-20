package com.june0122.sunflower.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.june0122.sunflower.data.entity.User
import kotlinx.coroutines.CoroutineScope

@Database(entities = [User::class], version = 1)
abstract class UserRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    private class UserDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // 앱 재실행 시에도 데이터를 유지하려면 아래의 코드 주석 처리
//            INSTANCE?.let { database ->
//                scope.launch(Dispatchers.IO) {
//                    populateDatabase(database.userDao())
//                }
//            }
        }

        suspend fun populateDatabase(userDao: UserDao) {
            userDao.deleteAll()

            // 디폴트 데이터 추가
//            var user = User("", "", "")
//            userDao.insert(user)
//            user = User("", "", "")
//            userDao.insert(user)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): UserRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java,
                    "user_database"
                )
                    .addCallback(UserDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

//    companion object {
//        private const val databaseName = "user_database"
//        private var appDatabase: UserRoomDatabase? = null
//
//        fun getDatabase(context: Context): UserRoomDatabase? {
//            if (appDatabase == null) {
//                appDatabase = Room.databaseBuilder(
//                    context,
//                    UserRoomDatabase::class.java,
//                    databaseName
//                ).build()
//            }
//
//            return appDatabase
//        }
//    }
}