package com.example.storyapp.Preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoryUserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getLoginAuth(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_LOGIN_ACTIVE] ?: false
        }
    }


    suspend fun savingLoginAuth(isLoginActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGIN_ACTIVE] = isLoginActive
        }
    }


    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }



    suspend fun savingTokenAuth(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    fun getUser(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_NAME] ?: ""
        }
    }


    suspend fun saveUser(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: StoryUserPreference? = null

        fun getUserPreference(dataStore: DataStore<Preferences>): StoryUserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryUserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }

        private val  IS_LOGIN_ACTIVE = booleanPreferencesKey("login_state")
        private val TOKEN = stringPreferencesKey("token")
        private val USER_NAME = stringPreferencesKey("name")

    }
}