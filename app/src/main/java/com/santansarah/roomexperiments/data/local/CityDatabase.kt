package com.example.roomlists.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import importList
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Database(entities = [City::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    /**
     * Here we pre-populate the database with a list from [importList].
     * CityDao runs a select count(*) query on the table.
     */
    private suspend fun populateInitialData() {
        if (cityDao().count() == 0) {
            cityDao().insertAll(importList)
        }
    }

    /**
     * Our database singleton. I'm using a Mutex, b/c synchronize will give us issues:
     * `The 'populateInitialData' suspension point is inside a critical section.`
     * Our mutex could potentially lead to deadlocks? But I think it's OK here...let me know in
     * the comments or GitHub Issues if you have alternative ideas. onCreate? callbacks?
     * I wonder if Hilt would help us relieve some of this...I'll have to try it sometime.
     * It might just be easier to use a db from assets or file...but I wanted to try it this way.
     */
    companion object {

        private var sInstance: AppDatabase? = null
        private val mutex = Mutex()

        suspend fun getInstance(context: Context): AppDatabase {
            mutex.withLock {
                if (sInstance == null) {
                    sInstance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "city-weather"
                        )
                        .build()
                    sInstance!!.populateInitialData()
                }
                return sInstance!!
            }
        }

    }
}