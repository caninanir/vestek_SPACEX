package com.can_inanir.spacex.data.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.can_inanir.spacex.data.local.entities.LaunchEntity
import com.can_inanir.spacex.data.repository.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingLaunchesViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    private val _upcomingLaunches = MutableStateFlow<List<LaunchEntity>>(emptyList())
    val upcomingLaunches: StateFlow<List<LaunchEntity>> = _upcomingLaunches

    init {
        fetchUpcomingLaunches()
    }

    private fun fetchUpcomingLaunches() {
        viewModelScope.launch {
            _upcomingLaunches.value = repository.getUpcomingLaunches()
        }
    }
}