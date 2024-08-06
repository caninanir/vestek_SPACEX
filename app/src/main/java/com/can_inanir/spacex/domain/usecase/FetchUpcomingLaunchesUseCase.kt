package com.can_inanir.spacex.domain.usecase

import com.can_inanir.spacex.domain.model.Launch
import com.can_inanir.spacex.domain.repository.SpaceXRepository

class FetchUpcomingLaunchesUseCase(private val repository: SpaceXRepository) {
    suspend fun invoke(): List<Launch> {
        return repository.getUpcomingLaunches()
    }
}