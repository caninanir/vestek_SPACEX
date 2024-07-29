package com.can_inanir.spacex.data.remote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.can_inanir.spacex.data.model.Launch
import com.can_inanir.spacex.data.model.Launchpad
import com.can_inanir.spacex.data.model.Rocket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FetchDataViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _rockets = MutableStateFlow<List<Rocket>>(emptyList())
    val rockets: StateFlow<List<Rocket>> = _rockets

    private val _selectedRocket = MutableStateFlow<Rocket?>(null)
    val selectedRocket: StateFlow<Rocket?> = _selectedRocket

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites

    private val _upcomingLaunches = MutableStateFlow<List<Launch>>(emptyList())
    val upcomingLaunches: StateFlow<List<Launch>> = _upcomingLaunches

    init {
        fetchRockets()
        fetchUpcomingLaunches()
        auth.currentUser?.let { fetchFavorites(it.email!!) }
    }

    private fun fetchRockets() {
        viewModelScope.launch {
            try {
                val fetchedRockets = RetrofitInstance.api.getRockets()
                _rockets.value = fetchedRockets
            } catch (e: Exception) {
                Log.e("RocketsViewModel", "Error fetching rockets", e)
            }
        }
    }

    private fun fetchUpcomingLaunches() {
        viewModelScope.launch {
            try {
                val fetchedLaunches = RetrofitInstance.api.getUpcomingLaunches()
                _upcomingLaunches.value = fetchedLaunches
            } catch (e: Exception) {
                Log.e("RocketsViewModel", "Error fetching upcoming launches", e)
            }
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

    fun fetchRocketById(id: String, onSuccess: (Rocket) -> Unit) {
        viewModelScope.launch {
            try {
                val rocket = RetrofitInstance.api.getRocket(id)
                onSuccess(rocket)
            } catch (e: Exception) {
                Log.e("RocketsViewModel", "Error fetching rocket by ID", e)
            }
        }
    }

    fun fetchRocketById(id: String) {
        viewModelScope.launch {
            try {
                val fetchedRocket = RetrofitInstance.api.getRocket(id)
                _selectedRocket.value = fetchedRocket
            } catch (e: Exception) {
                Log.e("RocketsViewModel", "Error fetching rocket by ID", e)
            }
        }
    }

    fun fetchLaunchpadById(id: String, onSuccess: (Launchpad) -> Unit) {
        viewModelScope.launch {
            try {
                val launchpad = RetrofitInstance.api.getLaunchpad(id)
                onSuccess(launchpad)
            } catch (e: Exception) {
                Log.e("RocketsViewModel", "Error fetching launchpad by ID", e)
            }
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