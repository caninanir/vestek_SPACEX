package com.can_inanir.spacex

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
interface ApiService {
    @GET("rockets")
    suspend fun getRockets(): List<Rocket>

    @GET("rockets/{id}")
    suspend fun getRocket(@Path("id") id: String): Rocket
}

object RetrofitInstance {
    private const val BASE_URL = "https://api.spacexdata.com/v4/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}