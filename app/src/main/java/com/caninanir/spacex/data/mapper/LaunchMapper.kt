package com.caninanir.spacex.data.mapper

import com.caninanir.spacex.data.local.entities.LaunchEntity
import com.caninanir.spacex.domain.model.Launch

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
