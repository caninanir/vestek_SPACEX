package com.can_inanir.spacex.data.model

import androidx.room.ColumnInfo

data class Launch(
    val id: String,
    val name: String,
    @ColumnInfo(name = "date_utc")
    val dateUtc: String,
    val rocket: String,
    val launchpad: String,
    val details: String?,
    val links: Links,
    val patches: Patches?,
) {
    data class Links(
        val webcast: String?,
        val article: String?,
        val wikipedia: String?,
        val flickr: FlickrImages
    ) {
        data class FlickrImages(
            val small: List<String>,
            val original: List<String>
        )
    }

    data class Patches(
        val small: String?,
        val large: String?
    )
}