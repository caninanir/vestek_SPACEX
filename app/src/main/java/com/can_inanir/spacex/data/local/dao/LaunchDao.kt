package com.can_inanir.spacex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.can_inanir.spacex.data.local.entities.LaunchEntity

@Dao
interface LaunchDao {
    @Query("SELECT * FROM launches")
    suspend fun getAllLaunches(): List<LaunchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunches(launches: List<LaunchEntity>)
}