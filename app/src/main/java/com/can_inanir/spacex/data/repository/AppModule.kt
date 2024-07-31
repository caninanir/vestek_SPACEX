package com.can_inanir.spacex.data.repository

import android.app.Application
import androidx.room.Room
import com.can_inanir.spacex.data.local.AppDatabase
import com.can_inanir.spacex.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "spacex_database")
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.spacexdata.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSpaceXRepository(apiService: ApiService, appDatabase: AppDatabase): SpaceXRepository {
        return SpaceXRepository(apiService, appDatabase)
    }
}