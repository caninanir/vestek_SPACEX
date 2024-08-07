package com.caninanir.spacex.data.mapper

import com.caninanir.spacex.data.local.entities.RocketEntity
import com.caninanir.spacex.domain.model.Rocket

fun RocketEntity.toDomain(): Rocket {
    return Rocket(
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
