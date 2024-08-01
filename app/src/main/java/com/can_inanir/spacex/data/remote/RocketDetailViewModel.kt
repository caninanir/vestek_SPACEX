package com.can_inanir.spacex.data.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.can_inanir.spacex.data.local.entities.RocketEntity
import com.can_inanir.spacex.data.repository.SpaceXRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RocketDetailViewModel @Inject constructor(
    private val repository: SpaceXRepository
) : ViewModel() {

    fun fetchRocketById(id: String, onSuccess: (RocketEntity) -> Unit) {
        viewModelScope.launch {
            val rocket = repository.getRocketById(id)
            onSuccess(rocket)
        }
    }
}