package com.example.socialapp.common.data

import androidx.datastore.core.DataStore
import com.example.socialapp.common.data.local.UserPreferences
import com.example.socialapp.common.data.local.UserSettings
import kotlinx.coroutines.flow.first


internal class AndroidUserPreferences(
    //личное хранилище данных
    private val dataStore: DataStore<UserSettings>
): UserPreferences {

    override suspend fun getUserData(): UserSettings {
        return dataStore.data.first() // первое извелечение данных из потока
    }

    override suspend fun setUserData(userSettings: UserSettings) {
        dataStore.updateData { userSettings } // вызовем хранилище данных и передадим настроки польз
    }

}

