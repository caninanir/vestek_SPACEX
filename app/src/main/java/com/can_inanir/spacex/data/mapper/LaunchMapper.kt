package com.can_inanir.spacex.data.mapper

import com.can_inanir.spacex.data.local.entities.LaunchEntity
import com.can_inanir.spacex.data.remote.dto.LaunchDto
import com.can_inanir.spacex.domain.model.Launch

fun LaunchDto.toEntity(): LaunchEntity {
    return LaunchEntity(
        id = this.id,
        name = this.name,
        dateUtc = this.dateUtc,
        rocket = this.rocket,
        launchpad = this.launchpad,
        details = this.details,
        links = this.links,
        patches = this.patches
    )
}

fun LaunchEntity.toDomain(): Launch {
    return Launch(
        id = this.id,
        name = this.name,
        dateUtc = this.dateUtc,
        rocket = this.rocket,
        launchpad = this.launchpad,
        details = this.details,
        links = this.links,
        patches = this.patches
    )
}