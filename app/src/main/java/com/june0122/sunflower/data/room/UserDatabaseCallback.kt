package com.june0122.sunflower.data.room

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope

class UserDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
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