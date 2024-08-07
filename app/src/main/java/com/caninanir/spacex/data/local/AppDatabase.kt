package com.caninanir.spacex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.caninanir.spacex.data.local.dao.LaunchDao
import com.caninanir.spacex.data.local.dao.LaunchpadDao
import com.caninanir.spacex.data.local.dao.RocketDao
import com.caninanir.spacex.data.local.entities.LaunchEntity
import com.caninanir.spacex.data.local.entities.LaunchpadEntity
import com.caninanir.spacex.data.local.entities.RocketEntity

@Database(
    entities = [RocketEntity::class, LaunchEntity::class, LaunchpadEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rocketDao(): RocketDao
    abstract fun launchDao(): LaunchDao
    abstract fun launchpadDao(): LaunchpadDao
}
