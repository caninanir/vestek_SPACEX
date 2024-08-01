package com.can_inanir.spacex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.can_inanir.spacex.data.model.Measurement
import com.can_inanir.spacex.data.model.PayloadWeight
import com.can_inanir.spacex.data.model.Weight

@Entity(tableName = "rockets")
data class RocketEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "first_flight") val firstFlight: String,
    @ColumnInfo(name = "cost_per_launch") val costPerLaunch: Long,
    @ColumnInfo(name = "success_rate_pct") val successRatePct: Int,
    @ColumnInfo(name = "wikipedia") val wikipedia: String,
    @ColumnInfo(name = "flickr_images") val flickrImages: List<String>,
    @ColumnInfo(name = "height") val height: Measurement,
    @ColumnInfo(name = "diameter") val diameter: Measurement,
    @ColumnInfo(name = "mass") val mass: Weight,
    @ColumnInfo(name = "payload_weights") val payloadWeights: List<PayloadWeight>
)