package com.can_inanir.spacex.domain.usecase

import com.can_inanir.spacex.domain.model.Launchpad
import com.can_inanir.spacex.domain.repository.SpaceXRepository

class FetchLaunchpadByIdUseCase(private val repository: SpaceXRepository) {
    suspend fun invoke(id: String): Launchpad {
        return repository.getLaunchpadById(id)
    }
}