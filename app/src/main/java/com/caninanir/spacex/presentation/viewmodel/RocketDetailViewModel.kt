package com.caninanir.spacex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caninanir.spacex.domain.model.Rocket
import com.caninanir.spacex.domain.usecase.FetchRocketByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RocketDetailViewModel @Inject constructor(
    private val fetchRocketByIdUseCase: FetchRocketByIdUseCase
) : ViewModel() {

    fun fetchRocketById(id: String, onSuccess: (Rocket) -> Unit) {
        viewModelScope.launch {
            val rocket = fetchRocketByIdUseCase.invoke(id)
            onSuccess(rocket)
        }
    }
}
