package com.can_inanir.spacex

data class Rocket(
    val id: String,
    val name: String,
    val description: String,
    val firstFlight: String,
    val costPerLaunch: Long,
    val successRatePct: Int,
    val wikipedia: String,
    val flickrImages: List<String>
)