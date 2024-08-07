package com.caninanir.spacex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caninanir.spacex.data.local.entities.LaunchpadEntity

@Dao
interface LaunchpadDao {
    @Query("SELECT * FROM launchpads WHERE id = :id")
    suspend fun getLaunchpadById(id: String): LaunchpadEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunchpad(launchpad: LaunchpadEntity)
}
