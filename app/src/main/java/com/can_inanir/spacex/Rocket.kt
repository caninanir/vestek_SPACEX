package com.can_inanir.spacex

data class Rocket(
    val id: String,
    val name: String,
    val description: String,
    val first_flight: String,
    val cost_per_launch: Long,
    val success_rate_pct: Int,
    val wikipedia: String,
    val flickr_images: List<String>
)