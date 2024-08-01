package com.can_inanir.spacex.data.remote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.can_inanir.spacex.data.local.entities.LaunchEntity
import com.can_inanir.spacex.data.local.entities.LaunchpadEntity
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.repository.SpaceXRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FetchDataViewModel @Inject constructor(
    private val repository: SpaceXRepository,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _rockets = MutableStateFlow<List<RocketEntity>>(emptyList())
    val rockets: StateFlow<List<RocketEntity>> = _rockets

    private val _upcomingLaunches = MutableStateFlow<List<LaunchEntity>>(emptyList())
    val upcomingLaunches: StateFlow<List<LaunchEntity>> = _upcomingLaunches

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites

    init {
        fetchRockets()
        fetchUpcomingLaunches()
        auth.currentUser?.let { fetchFavorites(it.email!!) }
    }

    private fun fetchRockets() {
        viewModelScope.launch {
            val fetchedRockets = repository.getRockets()
            _rockets.value = fetchedRockets
        }
    }

    private fun fetchUpcomingLaunches() {
        viewModelScope.launch {
            val fetchedLaunches = repository.getUpcomingLaunches()
            _upcomingLaunches.value = fetchedLaunches
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun fetchFavorites(userEmail: String) {
        val userDocRef = db.collection("users").document(userEmail)
        userDocRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                Log.w("RocketsViewModel", "Listen failed.", exception)
                return@addSnapshotListener
            }
            val favoritesMap = documentSnapshot?.data?.get("favorites") as? Map<String, Boolean> ?: emptyMap()
            val favoriteSet = favoritesMap.filter { it.value }.keys
            _favorites.value = favoriteSet
        }
    }

    fun fetchRocketById(id: String, onSuccess: (RocketEntity) -> Unit) {
        viewModelScope.launch {
            val rocket = repository.getRocketById(id)
            onSuccess(rocket)
        }
    }

    fun fetchLaunchpadById(id: String, onSuccess: (LaunchpadEntity) -> Unit) {
        viewModelScope.launch {
            val launchpad = repository.getLaunchpadById(id)
            onSuccess(launchpad)
        }
    }

    fun toggleFavorite(rocketName: String) {
        val userEmail = auth.currentUser?.email ?: return
        val userDocRef = db.collection("users").document(userEmail)
        if (_favorites.value.contains(rocketName)) {
            userDocRef.update("favorites.$rocketName", false)
        } else {
            userDocRef.update("favorites.$rocketName", true)
        }
    }
}
