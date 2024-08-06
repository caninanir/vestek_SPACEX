package com.can_inanir.spacex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.can_inanir.spacex.domain.model.Rocket
import com.can_inanir.spacex.domain.usecase.FetchRocketByIdUseCase
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