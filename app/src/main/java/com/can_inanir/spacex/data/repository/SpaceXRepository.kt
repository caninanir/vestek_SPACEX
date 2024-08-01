package com.can_inanir.spacex.data.repository

import com.can_inanir.spacex.data.local.AppDatabase
import com.can_inanir.spacex.data.local.entities.*
import com.can_inanir.spacex.data.model.Rocket
import com.can_inanir.spacex.data.model.Launch
import com.can_inanir.spacex.data.model.Launchpad
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

fun Rocket.toRocketEntity(): RocketEntity {
    return RocketEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        firstFlight = this.firstFlight,
        costPerLaunch = this.costPerLaunch,
        successRatePct = this.successRatePct,
        wikipedia = this.wikipedia,
        flickrImages = this.flickrImages,
        height = this.height,
        diameter = this.diameter,
        mass = this.mass,
        payloadWeights = this.payloadWeights
    )
}

fun Launch.toLaunchEntity(): LaunchEntity {
    return LaunchEntity(
        id = this.id,
        name = this.name,
        dateUtc = this.dateUtc,
        rocket = this.rocket,
        launchpad = this.launchpad,
        details = this.details,
        links = this.links,
        patches = this.patches
    )
}

@Suppress("unused")
fun Launchpad.toLaunchpadEntity(): LaunchpadEntity {
    return LaunchpadEntity(
        id = this.id,
        name = this.name,
        fullName = this.fullName,
        status = this.status,
        region = this.region,
        details = this.details,
        images = this.images
    )
}