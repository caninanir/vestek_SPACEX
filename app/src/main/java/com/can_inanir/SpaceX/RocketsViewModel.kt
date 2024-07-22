package com.example.a3rdtimesthecharm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RocketsViewModel : ViewModel() {
    private val _rockets = MutableStateFlow<List<Rocket>>(emptyList())
    val rockets: StateFlow<List<Rocket>> = _rockets

    private val _selectedRocket = MutableStateFlow<Rocket?>(null)
    val selectedRocket: StateFlow<Rocket?> = _selectedRocket

    init {
        fetchRockets()
    }

    private fun fetchRockets() {
        viewModelScope.launch {
            try {
                val fetchedRockets = RetrofitInstance.api.getRockets()
                _rockets.value = fetchedRockets
            } catch (e: Exception) {
                //handle errors later lol
            }
        }
    }

    fun fetchRocketById(id: String) {
        viewModelScope.launch {
            try {
                val fetchedRocket = RetrofitInstance.api.getRocket(id)
                _selectedRocket.value = fetchedRocket
            } catch (e: Exception) {
                //handle errors later lol
            }
        }
    }
}