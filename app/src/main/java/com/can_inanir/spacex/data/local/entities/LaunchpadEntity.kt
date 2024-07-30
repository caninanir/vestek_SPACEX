package com.can_inanir.spacex.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.can_inanir.spacex.data.model.Launchpad
@Suppress("PropertyName")
@Entity(tableName = "launchpads")
data class LaunchpadEntity(
    @PrimaryKey val id: String,
    val name: String,
    val full_name: String,
    val status: String,
    val region: String,
    val details: String,
    val images: Launchpad.LaunchpadImages
)