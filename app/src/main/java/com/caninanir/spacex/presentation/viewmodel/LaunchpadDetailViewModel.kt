package com.caninanir.spacex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caninanir.spacex.domain.model.Launchpad
import com.caninanir.spacex.domain.usecase.FetchLaunchpadByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchpadDetailViewModel @Inject constructor(
    private val fetchLaunchpadByIdUseCase: FetchLaunchpadByIdUseCase
) : ViewModel() {

    fun fetchLaunchpadById(id: String, onSuccess: (Launchpad) -> Unit) {
        viewModelScope.launch {
            val launchpad = fetchLaunchpadByIdUseCase.invoke(id)
            onSuccess(launchpad)
        }
    }
}
