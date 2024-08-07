package com.caninanir.spacex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caninanir.spacex.domain.model.Launch
import com.caninanir.spacex.domain.usecase.FetchUpcomingLaunchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingLaunchesViewModel @Inject constructor(
    private val fetchUpcomingLaunchesUseCase: FetchUpcomingLaunchesUseCase
) : ViewModel() {

    private val _upcomingLaunches = MutableStateFlow<List<Launch>>(emptyList())
    val upcomingLaunches: StateFlow<List<Launch>> = _upcomingLaunches

    init {
        fetchUpcomingLaunches()
    }

    private fun fetchUpcomingLaunches() {
        viewModelScope.launch {
            val launches = fetchUpcomingLaunchesUseCase.invoke()
            _upcomingLaunches.value = launches
        }
    }
}
