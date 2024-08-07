package com.caninanir.spacex.domain.model
import com.google.gson.annotations.SerializedName

data class Launchpad(
    val id: String,
    val name: String,
    @SerializedName("full_name") val fullName: String,
    val status: String,
    val region: String,
    val details: String,
    val images: LaunchpadImages
) {
    data class LaunchpadImages(
        val large: List<String>
    )
}
