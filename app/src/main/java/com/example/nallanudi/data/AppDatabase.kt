package com.example.nallanudi.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WordEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao
}