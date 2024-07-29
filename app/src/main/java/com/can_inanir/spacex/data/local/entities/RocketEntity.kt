package com.can_inanir.spacex.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.can_inanir.spacex.data.model.Measurement
import com.can_inanir.spacex.data.model.PayloadWeight
import com.can_inanir.spacex.data.model.Weight

@Entity(tableName = "rockets")
data class RocketEntity(
    @PrimaryKey val id: String,
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