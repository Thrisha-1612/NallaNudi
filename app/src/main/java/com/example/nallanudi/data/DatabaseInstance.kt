package com.example.nallanudi.data

import android.content.Context
import androidx.room.Room

object DatabaseInstance {

    @Volatile
    private var appDatabase: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {

        return appDatabase ?: synchronized(this) {

            val instance = appDatabase ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "nallanudi_db"
            )
                .fallbackToDestructiveMigration()
                .build()

            appDatabase = instance
            instance
        }
    }
}