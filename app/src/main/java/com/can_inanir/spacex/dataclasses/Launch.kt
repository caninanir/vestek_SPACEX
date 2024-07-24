package com.can_inanir.spacex.dataclasses
@Suppress("PropertyName")
data class Launch(
    val id: String,
    val name: String,
    val date_utc: String,
    val rocket: String,
    val launchpad: String,
    val details: String?,
    val links: Links
) {
    data class Links(
        val webcast: String?,
        val article: String?,
        val wikipedia: String?
    )
}