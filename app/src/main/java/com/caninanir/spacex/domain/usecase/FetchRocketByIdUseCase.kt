package com.caninanir.spacex.domain.usecase

import com.caninanir.spacex.domain.model.Rocket
import com.caninanir.spacex.domain.repository.SpaceXRepository

class FetchRocketByIdUseCase(private val repository: SpaceXRepository) {
    suspend fun invoke(id: String): Rocket {
        return repository.getRocketById(id)
    }
}
