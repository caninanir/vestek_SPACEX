package com.can_inanir.spacex.data.model

import androidx.room.ColumnInfo

data class Rocket(
    val id: String,
    val name: String,
    val description: String,
    @ColumnInfo(name = "first_flight")
    val firstFlight: String,
    @ColumnInfo(name = "cost_per_launch")
    val costPerLaunch: Long,
    @ColumnInfo(name = "success_rate_pct")
    val successRatePct: Int,
    val wikipedia: String,
    @ColumnInfo(name = "flickr_images")
    val flickrImages: List<String>,
    val height: Measurement,
    val diameter: Measurement,
    val mass: Weight,
    @ColumnInfo(name = "payload_weights")
    val payloadWeights: List<PayloadWeight>
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
