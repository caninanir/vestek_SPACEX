package com.can_inanir.spacex.domain.usecase

import com.can_inanir.spacex.domain.model.Rocket
import com.can_inanir.spacex.domain.repository.SpaceXRepository

class FetchRocketByIdUseCase(private val repository: SpaceXRepository) {
    suspend fun invoke(id: String): Rocket {
        return repository.getRocketById(id)
    }
}