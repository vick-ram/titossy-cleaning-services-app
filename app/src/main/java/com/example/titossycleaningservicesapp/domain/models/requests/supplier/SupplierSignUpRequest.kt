package com.example.titossycleaningservicesapp.domain.models.requests.supplier

import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus

data class SupplierSignUpRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val address: String,
    val email: String,
    val password: String
)

data class SupplierApproval(
    val status: ApprovalStatus
)