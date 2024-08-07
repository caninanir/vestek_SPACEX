package com.caninanir.spacex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caninanir.spacex.domain.model.Rocket
import com.caninanir.spacex.domain.usecase.FetchRocketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RocketListViewModel @Inject constructor(
    private val fetchRocketsUseCase: FetchRocketsUseCase
) : ViewModel() {

    private val _rockets = MutableStateFlow<List<Rocket>>(emptyList())
    val rockets: StateFlow<List<Rocket>> = _rockets

    init {
        fetchRockets()
    }

    private fun fetchRockets() {
        viewModelScope.launch {
            val rockets = fetchRocketsUseCase.invoke()
            _rockets.value = rockets
        }
    }
}