package com.can_inanir.spacex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.can_inanir.spacex.data.local.dao.*
import com.can_inanir.spacex.data.local.entities.*

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