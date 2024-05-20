package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreKeys: DataStoreKeys
) : ViewModel() {

    private val _startDestination: MutableState<String> = mutableStateOf(RootNavRoutes.ONBOARDING.route)
    val startDestination: State<String> = _startDestination

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading


    init {
    viewModelScope.launch {
        _isLoading.value = true
        val token = dataStoreKeys.getTokenFromDataStore()
        val authenticated = !token.isNullOrEmpty()
        val onboardingCompleted = dataStoreKeys.isOnBoardingCompleted().first()
        _isLoading.value = false

        _startDestination.value = when {
            onboardingCompleted && authenticated -> RootNavRoutes.HOME.route
            onboardingCompleted -> RootNavRoutes.AUTH.route
            else -> RootNavRoutes.ONBOARDING.route
        }
        _isLoading.value = false
    }
}

    fun setOnBoardingCompleted() {
        viewModelScope.launch {
            dataStoreKeys.setOnBoardingCompleted()
        }
    }
}