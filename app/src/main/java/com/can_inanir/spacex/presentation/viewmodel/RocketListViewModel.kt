package com.can_inanir.spacex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.domain.repository.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RocketListViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    private val _rockets = MutableStateFlow<List<RocketEntity>>(emptyList())
    val rockets: StateFlow<List<RocketEntity>> = _rockets

    init {
        fetchRockets()
    }

    private fun fetchRockets() {
        viewModelScope.launch {
            _rockets.value = repository.getRockets()
        }
    }
}