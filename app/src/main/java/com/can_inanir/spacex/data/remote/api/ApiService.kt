package com.can_inanir.spacex.data.remote.api

import com.can_inanir.spacex.domain.model.Launch
import com.can_inanir.spacex.domain.model.Launchpad
import com.can_inanir.spacex.domain.model.Rocket
import retrofit2.http.GET
import retrofit2.http.Path
interface ApiService {
    @GET("v4/rockets")
    suspend fun getRockets(): List<Rocket>

    @GET("v4/rockets/{id}")
    suspend fun getRocket(@Path("id") id: String): Rocket

    @GET("v5/launches/upcoming")
    suspend fun getUpcomingLaunches(): List<Launch>

    @GET("v4/launchpads/{id}")
    suspend fun getLaunchpad(@Path("id") id: String): Launchpad
}
