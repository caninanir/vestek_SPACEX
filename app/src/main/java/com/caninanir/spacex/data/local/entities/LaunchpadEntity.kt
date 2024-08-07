package com.caninanir.spacex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.caninanir.spacex.domain.model.Launchpad

@Entity(tableName = "launchpads")
data class LaunchpadEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "region") val region: String,
    @ColumnInfo(name = "details") val details: String,
    @ColumnInfo(name = "images") val images: Launchpad.LaunchpadImages
)

fun Launchpad.toLaunchpadEntity(): LaunchpadEntity {
    return LaunchpadEntity(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        status = this.status,
        region = this.region,
        details = this.details,
        images = this.images
    )
}
