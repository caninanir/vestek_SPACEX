package com.can_inanir.spacex.domain.repository

import com.can_inanir.spacex.domain.model.Launch
import com.can_inanir.spacex.domain.model.Launchpad
import com.can_inanir.spacex.domain.model.Rocket

interface SpaceXRepository {
    suspend fun getRockets(): List<Rocket>
    suspend fun getRocketById(id: String): Rocket
    suspend fun getUpcomingLaunches(): List<Launch>
    suspend fun getLaunchpadById(id: String): Launchpad
}