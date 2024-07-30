package com.can_inanir.spacex.data.repository

import android.content.Context
import androidx.room.Room
import com.can_inanir.spacex.data.local.AppDatabase
import com.can_inanir.spacex.data.local.dao.LaunchDao
import com.can_inanir.spacex.data.local.dao.LaunchpadDao
import com.can_inanir.spacex.data.local.dao.RocketDao
import com.can_inanir.spacex.data.remote.ApiService
import com.can_inanir.spacex.data.repository.SpaceXRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spacexdata.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "spacex_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideRocketDao(appDatabase: AppDatabase): RocketDao {
        return appDatabase.rocketDao()
    }

    @Singleton
    @Provides
    fun provideLaunchDao(appDatabase: AppDatabase): LaunchDao {
        return appDatabase.launchDao()
    }

    @Singleton
    @Provides
    fun provideLaunchpadDao(appDatabase: AppDatabase): LaunchpadDao {
        return appDatabase.launchpadDao()
    }

    @Singleton
    @Provides
    fun provideSpaceXRepository(
        apiService: ApiService,
        rocketDao: RocketDao,
        launchDao: LaunchDao,
        launchpadDao: LaunchpadDao
    ): SpaceXRepository {
        return SpaceXRepository(apiService, rocketDao, launchDao, launchpadDao)
    }
}