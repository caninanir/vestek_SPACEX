package com.can_inanir.spacex.data.remote

import com.can_inanir.spacex.data.model.Launch
import com.can_inanir.spacex.data.model.Launchpad
import com.can_inanir.spacex.data.model.Rocket
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

object RetrofitInstance {
    private const val BASE_URL = "https://api.spacexdata.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}