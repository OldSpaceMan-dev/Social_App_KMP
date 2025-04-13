package com.example.socialapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.socialapp.common.data.IOSUserPreferences
import com.example.socialapp.common.data.local.PREFERENCES_FILE_NAME
import com.example.socialapp.common.data.local.UserPreferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask


actual val platformModule = module {

    single<UserPreferences> { IOSUserPreferences(get()) }

    //пользовательские найстроки айос также зависят от хранилищя данных здесь

    // создаем хранилище данных
    single {
        createDatastore()
    }

}


//из за различия системных апи между айос/андройд нужно определить как создавать обьект хранилища
// данных для каждой платформы в отдельности


//определим factory метод, которыз создал обьект хранилища данныз для айос
// понадобится фабрика базы данных и путь к базе данных
// вернем фабрику данных предпочтений
@OptIn(ExperimentalForeignApi::class)
internal fun createDatastore(): DataStore<Preferences> {
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


