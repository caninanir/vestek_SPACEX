package com.can_inanir.spacex.data.remote.dto

import com.can_inanir.spacex.domain.model.Measurement
import com.can_inanir.spacex.domain.model.PayloadWeight
import com.can_inanir.spacex.domain.model.Weight
import com.google.gson.annotations.SerializedName

data class RocketDto(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("first_flight") val firstFlight: String,
    @SerializedName("cost_per_launch") val costPerLaunch: Long,
    @SerializedName("success_rate_pct") val successRatePct: Int,
    val wikipedia: String,
    @SerializedName("flickr_images") val flickrImages: List<String>,
    val height: Measurement,
    val diameter: Measurement,
    val mass: Weight,
    @SerializedName("payload_weights") val payloadWeights: List<PayloadWeight>
)