package com.example.socialapp.common.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.socialapp.common.data.local.PREFERENCES_FILE_NAME
import com.example.socialapp.common.data.local.UserPreferences
import com.example.socialapp.common.data.local.UserSettings
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

internal class IOSUserPreferences(
    // не можем напрямую передавать UserSettings - тк нет сериализатора
    // передаем хранилище данных с настройками типа
    private val dataStore: DataStore<Preferences>
): UserPreferences {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getUserData(): UserSettings {
        val preferences = dataStore.data.first()
        val userSettingsJson = preferences[USER_SETTINGS_KEY]?.firstOrNull() ?: return UserSettings()
        return json.decodeFromString(userSettingsJson)
    }


    override suspend fun setUserData(userSettings: UserSettings) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[USER_SETTINGS_KEY] = setOf(json.encodeToString(userSettings))
        }
    }

    companion object {
        private val USER_SETTINGS_KEY = stringSetPreferencesKey("user_settings")
    }

}










