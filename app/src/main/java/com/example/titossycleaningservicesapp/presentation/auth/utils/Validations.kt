package com.example.titossycleaningservicesapp.presentation.auth.utils

object Validations {
    fun isEmailValid(email: String): ValidationState {
        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ValidationState.Valid
        } else {
            ValidationState.Invalid("Invalid email address")
        }
    }

    fun isPasswordValid(password: String): ValidationState {
        return if (password.isEmpty()){
            ValidationState.Idle
        }else if (password.startsWith(password.lowercase())) {
            ValidationState.Invalid("Password must start with least one uppercase letter")
        } else if (password.trim().length < 8) {
            ValidationState.Invalid("Password must be at least 8 characters long")
        } else if (!password.contains(Regex(".*[0-9].*"))) {
            ValidationState.Invalid("Password must contain at least one digit")
        } else {
            ValidationState.Valid
        }
    }

    fun isPhoneValid(phone: String): ValidationState {
        val validPhoneFormat = phone.startsWith("07") || phone.startsWith("01")
        return if (!validPhoneFormat){
            ValidationState.Invalid("Should start with 07 or 01")
        } else if (phone.trim().length < 10) {
            ValidationState.Invalid("Phone number must be at least 10 characters long")
        } else if (phone.trim().length > 10) {
            ValidationState.Invalid("Invalid length")
        } else {
            ValidationState.Valid
        }
    }

    fun isValidName(name: String): ValidationState {
        return if (name.trim().isEmpty()) {
            ValidationState.Invalid("cannot be an empty field")
        } else {
            ValidationState.Valid
        }
    }
}

sealed class ValidationState {
    data object Idle: ValidationState()
    data object Valid : ValidationState()
    data class Invalid(val message: String) : ValidationState()
}
