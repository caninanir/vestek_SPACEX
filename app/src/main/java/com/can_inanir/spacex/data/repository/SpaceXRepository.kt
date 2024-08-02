package com.can_inanir.spacex.data.repository

import com.can_inanir.spacex.data.local.AppDatabase
import com.can_inanir.spacex.data.local.entities.LaunchEntity
import com.can_inanir.spacex.data.local.entities.LaunchpadEntity
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.local.entities.toLaunchEntity
import com.can_inanir.spacex.data.local.entities.toRocketEntity
import com.can_inanir.spacex.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpaceXRepository @Inject constructor(private val apiService: ApiService, private val appDatabase: AppDatabase) {

    suspend fun getRockets(): List<RocketEntity> {
        val cachedRockets = appDatabase.rocketDao().getAllRockets()
        return cachedRockets.ifEmpty {
            val rocketsFromApi = apiService.getRockets()
            val rocketEntities = rocketsFromApi.map {
                it.toRocketEntity()
            }
            appDatabase.rocketDao().insertRockets(rocketEntities)
            rocketEntities
        }
    }

    suspend fun getRocketById(id: String): RocketEntity {
        val cachedRocket = appDatabase.rocketDao().getRocketById(id)
        return cachedRocket ?: run {
            val rocketFromApi = apiService.getRocket(id)
            val rocketEntity = rocketFromApi.toRocketEntity()
            appDatabase.rocketDao().insertRocket(rocketEntity)
            rocketEntity
        }
    }

    suspend fun getUpcomingLaunches(): List<LaunchEntity> {
        val cachedLaunches = appDatabase.launchDao().getAllLaunches()
        return cachedLaunches.ifEmpty {
            val launchesFromApi = apiService.getUpcomingLaunches()
            val launchEntities = launchesFromApi.map {
                it.toLaunchEntity()
            }
            appDatabase.launchDao().insertLaunches(launchEntities)
            launchEntities
        }
    }

    suspend fun getLaunchpadById(id: String): LaunchpadEntity {
        val cachedLaunchpad = appDatabase.launchpadDao().getLaunchpadById(id)
        return cachedLaunchpad
    }
}
