package com.can_inanir.spacex.data.remote.dto

import com.can_inanir.spacex.domain.model.Launch
import com.google.gson.annotations.SerializedName

data class LaunchDto(
    val id: String,
    val name: String,
    @SerializedName("date_utc") val dateUtc: String,
    val rocket: String,
    val launchpad: String,
    val details: String?,
    val links: Launch.Links,
    val patches: Launch.Patches?
)