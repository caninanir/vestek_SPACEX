package com.can_inanir.spacex.data.repository_impl

import com.can_inanir.spacex.data.local.AppDatabase
import com.can_inanir.spacex.data.local.entities.toLaunchEntity
import com.can_inanir.spacex.data.local.entities.toLaunchpadEntity
import com.can_inanir.spacex.data.local.entities.toRocketEntity
import com.can_inanir.spacex.data.mapper.toDomain
import com.can_inanir.spacex.data.remote.api.ApiService
import com.can_inanir.spacex.domain.model.Launch
import com.can_inanir.spacex.domain.model.Launchpad
import com.can_inanir.spacex.domain.model.Rocket
import com.can_inanir.spacex.domain.repository.SpaceXRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpaceXRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : SpaceXRepository {

    override suspend fun getRockets(): List<Rocket> {
        return withContext(Dispatchers.IO) {
            val cachedRockets = appDatabase.rocketDao().getAllRockets()
            if (cachedRockets.isNotEmpty()) {
                return@withContext cachedRockets.map { it.toDomain() }
            }

            val rocketsFromApi = apiService.getRockets()
            val rocketEntities = rocketsFromApi.map { it.toRocketEntity() }

            appDatabase.rocketDao().insertRockets(rocketEntities)
            rocketEntities.map { it.toDomain() }
        }
    }

    override suspend fun getRocketById(id: String): Rocket {
        return withContext(Dispatchers.IO) {
            val cachedRocket = appDatabase.rocketDao().getRocketById(id)
            if (cachedRocket != null) {
                return@withContext cachedRocket.toDomain()
            }

            val rocketFromApi = apiService.getRocket(id)
            val rocketEntity = rocketFromApi.toRocketEntity()

            appDatabase.rocketDao().insertRocket(rocketEntity)
            return@withContext rocketEntity.toDomain()
        }
    }

    override suspend fun getUpcomingLaunches(): List<Launch> {
        return withContext(Dispatchers.IO) {
            val cachedLaunches = appDatabase.launchDao().getAllLaunches()
            if (cachedLaunches.isNotEmpty()) {
                return@withContext cachedLaunches.map { it.toDomain() }
            }

            val launchesFromApi = apiService.getUpcomingLaunches()
            val launchEntities = launchesFromApi.map { it.toLaunchEntity() }

            appDatabase.launchDao().insertLaunches(launchEntities)
            launchEntities.map { it.toDomain() }
        }
    }

    override suspend fun getLaunchpadById(id: String): Launchpad {
        return withContext(Dispatchers.IO) {
            val cachedLaunchpad = appDatabase.launchpadDao().getLaunchpadById(id)
            if (cachedLaunchpad != null) {
                return@withContext cachedLaunchpad.toDomain()
            }

            val launchpadFromApi = apiService.getLaunchpad(id)
            val launchpadEntity = launchpadFromApi.toLaunchpadEntity()

            appDatabase.launchpadDao().insertLaunchpad(launchpadEntity)
            return@withContext launchpadEntity.toDomain()
        }
    }
}