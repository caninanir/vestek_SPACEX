package com.can_inanir.spacex.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.can_inanir.spacex.domain.model.Measurement
import com.can_inanir.spacex.domain.model.PayloadWeight
import com.can_inanir.spacex.domain.model.Rocket
import com.can_inanir.spacex.domain.model.Weight

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

fun Rocket.toRocketEntity(): RocketEntity {
    return RocketEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        firstFlight = this.firstFlight,
        costPerLaunch = this.costPerLaunch,
        successRatePct = this.successRatePct,
        wikipedia = this.wikipedia,
        flickrImages = this.flickrImages,
        height = this.height,
        diameter = this.diameter,
        mass = this.mass,
        payloadWeights = this.payloadWeights
    )
}
