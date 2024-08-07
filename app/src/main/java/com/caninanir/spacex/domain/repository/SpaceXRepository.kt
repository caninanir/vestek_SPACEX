package com.caninanir.spacex.domain.repository

import com.caninanir.spacex.domain.model.Launch
import com.caninanir.spacex.domain.model.Launchpad
import com.caninanir.spacex.domain.model.Rocket

interface SpaceXRepository {
    suspend fun getRockets(): List<Rocket>
    suspend fun getRocketById(id: String): Rocket
    suspend fun getUpcomingLaunches(): List<Launch>
    suspend fun getLaunchpadById(id: String): Launchpad
}
