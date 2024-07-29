package com.can_inanir.spacex.data.model

@Suppress("PropertyName")
data class Rocket(
    val id: String,
    val name: String,
    val description: String,
    val first_flight: String,
    val cost_per_launch: Long,
    val success_rate_pct: Int,
    val wikipedia: String,
    val flickr_images: List<String>,
    val height: Measurement,
    val diameter: Measurement,
    val mass: Weight,
    val payload_weights: List<PayloadWeight>
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
