package com.caninanir.spacex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class FavoritesViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    open val favorites: StateFlow<Set<String>> = _favorites

    init {
        auth.currentUser?.let {
            fetchFavorites(it.email!!)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun fetchFavorites(userEmail: String) {
        val userDocRef = db.collection("users").document(userEmail)
        userDocRef.addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                Timber.tag("FavoritesViewModel").w(exception, "Listen failed.")
                return@addSnapshotListener
            }
            val favoritesMap = documentSnapshot?.data?.get("favorites") as? Map<String, Boolean> ?: emptyMap()
            _favorites.value = favoritesMap.filter { it.value }.keys
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
