package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BookingSharedViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _sharedData = MutableStateFlow(savedStateHandle.get<String>("sharedData") ?: "Initial Data")
    val sharedData: StateFlow<String> = _sharedData

    fun updateData(newData: String) {
        _sharedData.value = newData
        savedStateHandle["sharedData"] = newData
    }
}
