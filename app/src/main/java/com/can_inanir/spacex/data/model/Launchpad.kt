package com.can_inanir.spacex.data.model

@Suppress("PropertyName")
data class Launchpad(
    val id: String,
    val name: String,
    val full_name: String,
    val status: String,
    val region: String,
    val details: String,
    val images: LaunchpadImages
) {
    data class LaunchpadImages(
        val large: List<String>
    )
}