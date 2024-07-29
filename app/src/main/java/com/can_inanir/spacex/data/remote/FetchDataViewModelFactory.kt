package com.can_inanir.spacex.data.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.can_inanir.spacex.data.repository.SpaceXRepository

class FetchDataViewModelFactory(private val repository: SpaceXRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FetchDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FetchDataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}