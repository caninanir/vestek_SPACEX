package com.caninanir.spacex.domain.usecase

import com.caninanir.spacex.domain.model.Rocket
import com.caninanir.spacex.domain.repository.SpaceXRepository

class FetchRocketsUseCase(private val repository: SpaceXRepository) {
    suspend fun invoke(): List<Rocket> {
        return repository.getRockets()
    }
}
