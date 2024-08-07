package com.caninanir.spacex.domain.usecase

import com.caninanir.spacex.domain.model.Launch
import com.caninanir.spacex.domain.repository.SpaceXRepository

class FetchUpcomingLaunchesUseCase(private val repository: SpaceXRepository) {
    suspend fun invoke(): List<Launch> {
        return repository.getUpcomingLaunches()
    }
}
