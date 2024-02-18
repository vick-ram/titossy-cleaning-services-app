package com.example.titossycleaningservicesapp.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object Constants {
    private const val USER_PREFS_NAME = "user_prefs_name"
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(USER_PREFS_NAME)

}