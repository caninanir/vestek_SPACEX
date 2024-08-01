package com.can_inanir.spacex.data.model

import androidx.room.ColumnInfo

data class Launchpad(
    val id: String,
    val name: String,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    val status: String,
    val region: String,
    val details: String,
    val images: LaunchpadImages
) {
    data class LaunchpadImages(
        val large: List<String>
    )
}