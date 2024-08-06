package com.can_inanir.spacex.domain.model

import com.google.gson.annotations.SerializedName

data class Rocket(
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

data class Measurement(
    val meters: Double,
    val feet: Double
)

data class Weight(
    val kg: Double,
    val lb: Double
)

data class PayloadWeight(
    val id: String,
    val name: String,
    val kg: Double,
    val lb: Double
)