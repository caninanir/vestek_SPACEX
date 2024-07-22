package com.example.a3rdtimesthecharm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RocketsViewModel : ViewModel() {
    private val _rockets = MutableStateFlow<List<Rocket>>(emptyList())
    val rockets: StateFlow<List<Rocket>> = _rockets

    init {
        fetchRockets()
    }

    private fun fetchRockets() {
        viewModelScope.launch {
            try {
                val fetchedRockets = RetrofitInstance.api.getRockets()
                _rockets.value = fetchedRockets
            } catch (e: Exception) {
                //handle error lol
            }
        }
    }
}