package com.example.titossycleaningservicesapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object Constants {
    private const val USER_PREFS_NAME = "settings"
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(USER_PREFS_NAME)
    const val ONBOARDING_KEY = "onboarding"
    const val LOGIN_KEY = "login_key"
}