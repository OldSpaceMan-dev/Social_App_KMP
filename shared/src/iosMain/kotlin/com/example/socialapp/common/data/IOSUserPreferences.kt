package com.example.socialapp.common.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.socialapp.common.data.local.PREFERENCES_FILE_NAME
import com.example.socialapp.common.data.local.UserPreferences
import com.example.socialapp.common.data.local.UserSettings
import kotlinx.cinterop.ExperimentalForeignApi
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

    override suspend fun getUserData(): UserSettings {
        TODO("Not yet implemented")
    }


    override suspend fun setUserData(userSettings: UserSettings) {
        TODO("Not yet implemented")
    }

}


//из за различия системных апи между айос/андройд нужно определить как создавать обьект хранилища
// данных для каждой платформы в отдельности


//определим factory метод, которыз создал обьект хранилища данныз для айос
// понадобится фабрика базы данных и путь к базе данных
// вернем фабрику данных предпочтений
@OptIn(ExperimentalForeignApi::class)
internal fun createDatastore(): DataStore<Preferences>{
    return PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null, //обработчик
        migrations = emptyList(),
        produceFile = { // лямбда-код для создания системного файла единой системы
            // как создавать файл на диске айос
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            (requireNotNull(documentDirectory).path + "/$PREFERENCES_FILE_NAME").toPath()
        }
    )
}

// coin - как предоставлять пользовательские настроки и экземпляры хранилища данных для каждой платф











