package com.example.titossycleaningservicesapp.data.remote.util

class ErrorEvent<T>(val error: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            error
        }
    }

    fun peekContent(): T = error
}
