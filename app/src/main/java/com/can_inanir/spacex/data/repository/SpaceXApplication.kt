package com.can_inanir.spacex.data.repository

import android.app.Application
import androidx.room.Room
import com.can_inanir.spacex.data.local.AppDatabase
import com.can_inanir.spacex.data.remote.RetrofitInstance

class SpaceXApplication : Application() {
    private lateinit var database: AppDatabase
    lateinit var repository: SpaceXRepository

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "spacex_database"
        ).build()

        val apiService = RetrofitInstance.api
        repository = SpaceXRepository(apiService, database)
    }
}
