@file:Suppress("deprecation")

package com.caninanir.spacex.di

import android.app.Application
import androidx.room.Room
import com.caninanir.spacex.R
import com.caninanir.spacex.data.local.AppDatabase
import com.caninanir.spacex.data.remote.api.ApiService
import com.caninanir.spacex.data.repositoryimpl.SpaceXRepositoryImpl
import com.caninanir.spacex.domain.repository.SpaceXRepository
import com.caninanir.spacex.domain.usecase.FetchLaunchpadByIdUseCase
import com.caninanir.spacex.domain.usecase.FetchRocketByIdUseCase
import com.caninanir.spacex.domain.usecase.FetchRocketsUseCase
import com.caninanir.spacex.domain.usecase.FetchUpcomingLaunchesUseCase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        return Room.databaseBuilder(app, AppDatabase::class.java, "spacex_database").build()
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
    fun provideSpaceXRepository(
        apiService: ApiService,
        appDatabase: AppDatabase
    ): SpaceXRepository {
        return SpaceXRepositoryImpl(apiService, appDatabase)
    }

    // Provide FetchRocketByIdUseCase
    @Provides
    @Singleton
    fun provideFetchRocketByIdUseCase(repository: SpaceXRepository): FetchRocketByIdUseCase {
        return FetchRocketByIdUseCase(repository)
    }

    // Provide FetchRocketsUseCase
    @Provides
    @Singleton
    fun provideFetchRocketsUseCase(repository: SpaceXRepository): FetchRocketsUseCase {
        return FetchRocketsUseCase(repository)
    }

    // Provide FetchUpcomingLaunchesUseCase
    @Provides
    @Singleton
    fun provideFetchUpcomingLaunchesUseCase(repository: SpaceXRepository): FetchUpcomingLaunchesUseCase {
        return FetchUpcomingLaunchesUseCase(repository)
    }

    // Provide FetchLaunchpadByIdUseCase
    @Provides
    @Singleton
    fun provideFetchLaunchpadByIdUseCase(repository: SpaceXRepository): FetchLaunchpadByIdUseCase {
        return FetchLaunchpadByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideGoogleSignInClient(app: Application): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(app, gso)
    }
}