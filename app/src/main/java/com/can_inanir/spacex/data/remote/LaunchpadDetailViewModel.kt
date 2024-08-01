package com.can_inanir.spacex.data.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.can_inanir.spacex.data.local.entities.LaunchpadEntity
import com.can_inanir.spacex.data.repository.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchpadDetailViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    fun fetchLaunchpadById(id: String, onSuccess: (LaunchpadEntity) -> Unit) {
        viewModelScope.launch {
            val launchpad = repository.getLaunchpadById(id)
            onSuccess(launchpad)
        }
    }
}