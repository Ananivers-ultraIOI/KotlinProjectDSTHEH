package be.heh.kotlinproject_dstheh.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserRecord::class,MaterialsRecord::class], version = 1)
abstract class MyDB:RoomDatabase() {
    abstract fun userDao():UserDao
    abstract fun materialsDao():MaterialsDao
}