package com.can_inanir.spacex.data.mapper

import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.remote.dto.RocketDto

import com.can_inanir.spacex.domain.model.Rocket

fun RocketDto.toEntity(): RocketEntity {
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