package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.repository.EmployeeRepository
import com.example.titossycleaningservicesapp.presentation.auth.utils.ValidationState
import com.example.titossycleaningservicesapp.presentation.auth.utils.Validations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository
) : ViewModel() {


    private val _passwordState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val passwordState: StateFlow<ValidationState> = _passwordState.asStateFlow()

    private val _usernameOrEmailState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    val usernameOrEmailState: StateFlow<ValidationState> = _usernameOrEmailState.asStateFlow()

    private val _usernameOrEmailErrorMessage = MutableStateFlow("")
    val usernameOrEmailErrorMessage: StateFlow<String> = _usernameOrEmailErrorMessage.asStateFlow()

    private val _passwordErrorMessage = MutableStateFlow("")
    val passwordErrorMessage: StateFlow<String> = _passwordErrorMessage.asStateFlow()


    private val _resultChannel = Channel<AuthEvent>(Channel.BUFFERED)
    val resultChannel = _resultChannel.receiveAsFlow()

    private fun signIn() = viewModelScope.launch {
    }

    private fun update() = viewModelScope.launch {

    }

    private fun signOut() = viewModelScope.launch {

    }


    fun onPasswordChange(newPassword: String) {
        viewModelScope.launch {
            _passwordState.value = Validations.isPasswordValid(newPassword)
            if (_passwordState.value is ValidationState.Valid) {
                _passwordErrorMessage.emit("")
            } else if (_passwordState.value is ValidationState.Invalid) {
                _passwordErrorMessage.emit((_passwordState.value as ValidationState.Invalid).message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _resultChannel.close()
    }

}