package com.can_inanir.spacex

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RocketsViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _rockets = MutableStateFlow<List<Rocket>>(emptyList())
    val rockets: StateFlow<List<Rocket>> = _rockets

    private val _selectedRocket = MutableStateFlow<Rocket?>(null)
    val selectedRocket: StateFlow<Rocket?> = _selectedRocket

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites

    init {
        fetchRockets()
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