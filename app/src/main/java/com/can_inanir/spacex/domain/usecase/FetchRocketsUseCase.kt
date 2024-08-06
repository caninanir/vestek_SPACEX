package com.can_inanir.spacex.domain.usecase

import com.can_inanir.spacex.domain.model.Rocket
import com.can_inanir.spacex.domain.repository.SpaceXRepository

class FetchRocketsUseCase(private val repository: SpaceXRepository) {
    suspend fun invoke(): List<Rocket> {
        return repository.getRockets()
    }
}