package com.can_inanir.spacex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.can_inanir.spacex.domain.model.Launch

@Entity(tableName = "launches")
data class LaunchEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date_utc") val dateUtc: String,
    @ColumnInfo(name = "rocket") val rocket: String,
    @ColumnInfo(name = "launchpad") val launchpad: String,
    @ColumnInfo(name = "details") val details: String?,
    @ColumnInfo(name = "links") val links: Launch.Links,
    @ColumnInfo(name = "patches") val patches: Launch.Patches?
)

fun Launch.toLaunchEntity(): LaunchEntity {
    return LaunchEntity(
        id = this.id,
        name = this.name,
        dateUtc = this.dateUtc,
        rocket = this.rocket,
        launchpad = this.launchpad,
        details = this.details,
        links = this.links,
        patches = this.patches
    )
}
