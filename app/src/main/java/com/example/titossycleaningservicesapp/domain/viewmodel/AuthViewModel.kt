package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.data.repo.AuthenticationRepository
import com.example.titossycleaningservicesapp.data.utils.Resource
import com.example.titossycleaningservicesapp.domain.viewmodel.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    var currentUser = authenticationRepository.currentUser
        private set

    private val _signUpState = MutableStateFlow(UiState())
    val signUpState = _signUpState.asStateFlow()

    private val _signInState = MutableStateFlow(UiState())
    val signInState = _signInState.asStateFlow()

    private val _signOutState = MutableStateFlow(UiState())
    val signOutState = _signOutState.asStateFlow()

    init {
        if (currentUser != null) {
            viewModelScope.launch {
                _signInState.update { it.copy(isSuccess = "$currentUser") }

            }
        }
    }


    fun signIn(email: String, password: String) = viewModelScope.launch {
        val response = authenticationRepository.loginUser(email, password)
        response.collect { res ->
            when (res) {
                is Resource.Failure -> {
                    _signInState.update {
                        it.copy(errorMessage = "${res.message}")
                    }
                }

                is Resource.Loading -> {
                    _signInState.update {
                        it.copy(isLoading = true)
                    }
                }

                is Resource.Success -> {
                    _signInState.update {
                        it.copy(isSuccess = "${res.data}")
                    }
                }
            }
        }
    }

    fun signUp(
        userId: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) = viewModelScope.launch {
        val response =
            authenticationRepository.registerUser(userId, firstName, lastName, email, password)
        response.collect { res ->
            when (res) {
                is Resource.Failure -> {
                    _signUpState.update {
                        it.copy(errorMessage = "${res.message}")
                    }
                }

                is Resource.Loading -> {
                    _signUpState.update {
                        it.copy(isLoading = true)
                    }
                }

                is Resource.Success -> {
                    _signUpState.update {
                        it.copy(isSuccess = "${res.data}")
                    }
                }
            }
        }
    }

    fun signOut() = viewModelScope.launch {
        val response = authenticationRepository.signOut()

        response.collect { res ->
            when (res) {
                is Resource.Loading -> {
                    _signOutState.update {
                        it.copy(isLoading = true)
                    }
                }

                is Resource.Failure -> {
                    _signOutState.update {
                        it.copy(errorMessage = res.message.toString())
                    }
                }

                is Resource.Success -> {
                    _signOutState.update {
                        it.copy(isSuccess = res.data.toString())
                    }
                }
            }
        }
    }

    val onBoardingCompleted: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
        }

    suspend fun setOnboardingCompleted() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = true
        }
    }

    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("on_boarding_completed")
    }
}