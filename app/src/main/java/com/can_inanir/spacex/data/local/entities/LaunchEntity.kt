package com.can_inanir.spacex.data.local.entities



import androidx.room.Entity
import androidx.room.PrimaryKey
import com.can_inanir.spacex.data.model.Launch

@Entity(tableName = "launches")
data class LaunchEntity(
    @PrimaryKey val id: String,
    val name: String,
    val date_utc: String,
    val rocket: String,
    val launchpad: String,
    val details: String?,
    val links: Launch.Links,
    val patches: Launch.Patches?
)