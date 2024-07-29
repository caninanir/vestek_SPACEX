package com.can_inanir.spacex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.can_inanir.spacex.data.local.entities.RocketEntity

@Dao
interface RocketDao {
    @Query("SELECT * FROM rockets")
    suspend fun getAllRockets(): List<RocketEntity>

    @Query("SELECT * FROM rockets WHERE id = :id")
    suspend fun getRocketById(id: String): RocketEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRockets(rockets: List<RocketEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRocket(rocket: RocketEntity)
}