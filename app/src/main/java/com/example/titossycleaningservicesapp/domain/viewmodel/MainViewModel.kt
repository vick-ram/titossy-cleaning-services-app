package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.presentation.auth.utils.decodeToken
import com.example.titossycleaningservicesapp.presentation.utils.Authentication
import com.example.titossycleaningservicesapp.presentation.utils.RootNavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreKeys: DataStoreKeys
) : ViewModel() {

    private val _startDestination: MutableState<String> =
        mutableStateOf(RootNavRoutes.ONBOARDING.route)
    val startDestination: State<String> = _startDestination

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        viewModelScope.launch {

            val token = dataStoreKeys.getTokenFromDataStore()
            val authenticated = !token.isNullOrEmpty()
            val onboardingCompleted = dataStoreKeys.isOnBoardingCompleted().first()
            val approvalStatus = dataStoreKeys.getApprovalStatusFromDataStore()

            delay(2000)
            _startDestination.value = when {
                onboardingCompleted && authenticated
                        && approvalStatus == ApprovalStatus.APPROVED.name -> RootNavRoutes.HOME.route
                onboardingCompleted && authenticated && approvalStatus == ApprovalStatus.PENDING.name -> Authentication.APPROVAL.route
                onboardingCompleted -> RootNavRoutes.AUTH.route
                else -> RootNavRoutes.ONBOARDING.route
            }
            delay(1000L)
            _isReady.value = true
        }
    }

    fun setOnBoardingCompleted() {
        viewModelScope.launch {
            dataStoreKeys.setOnBoardingCompleted()
        }
    }

    suspend fun readUserFromToken(): Pair<String, String> {
        val token = dataStoreKeys.getTokenFromDataStore()
        val decodeToken = token?.let { decodeToken(it) }
        val username = decodeToken?.get("username") as? String
        val email = decodeToken?.get("email") as? String
        return Pair(username ?: "", email ?: "")
    }
}