package com.caninanir.spacex.data.mapper

import com.caninanir.spacex.data.local.entities.LaunchpadEntity
import com.caninanir.spacex.domain.model.Launchpad

fun LaunchpadEntity.toDomain(): Launchpad {
    return Launchpad(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        status = this.status,
        region = this.region,
        details = this.details,
        images = this.images
    )
}
