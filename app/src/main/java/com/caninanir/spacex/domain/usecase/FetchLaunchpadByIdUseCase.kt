package com.caninanir.spacex.domain.usecase

import com.caninanir.spacex.domain.model.Launchpad
import com.caninanir.spacex.domain.repository.SpaceXRepository

class FetchLaunchpadByIdUseCase(private val repository: SpaceXRepository) {
    suspend fun invoke(id: String): Launchpad {
        return repository.getLaunchpadById(id)
    }
}
