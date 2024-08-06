package com.can_inanir.spacex.data.mapper

import com.can_inanir.spacex.data.remote.dto.LaunchpadDto
import com.can_inanir.spacex.data.local.entities.LaunchpadEntity
import com.can_inanir.spacex.domain.model.Launchpad

fun LaunchpadDto.toEntity(): LaunchpadEntity {
    return LaunchpadEntity(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        status = this.status,
        region = this.region,
        details = this.details,
        images = this.images
    )
}

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