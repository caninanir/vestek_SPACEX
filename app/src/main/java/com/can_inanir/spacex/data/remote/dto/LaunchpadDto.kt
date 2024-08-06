package com.can_inanir.spacex.data.remote.dto

import com.can_inanir.spacex.domain.model.Launchpad
import com.google.gson.annotations.SerializedName

data class LaunchpadDto(
    val id: String,
    val name: String,
    @SerializedName("full_name") val fullName: String,
    val status: String,
    val region: String,
    val details: String,
    val images: Launchpad.LaunchpadImages
)