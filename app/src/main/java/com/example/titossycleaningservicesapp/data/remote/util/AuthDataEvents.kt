package com.example.titossycleaningservicesapp.data.remote.util

import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus

sealed class AuthEvent {
    data object Loading : AuthEvent()
    data class Success(val message: String? = null, val approvalStatus: ApprovalStatus? = null) : AuthEvent()
    data class Error(val message: String) : AuthEvent()
}

