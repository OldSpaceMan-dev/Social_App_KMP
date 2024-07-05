package com.example.socialapp.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.socialapp.common.data.AndroidUserPreferences
import com.example.socialapp.common.data.UserSettingsSerializer
import com.example.socialapp.common.data.local.PREFERENCES_FILE_NAME
import com.example.socialapp.common.data.local.UserPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<UserPreferences> { AndroidUserPreferences(get()) }

    //нужно хранилище данных
    //указать coin как создать экземпляр хранилищя данных для андройд

    // get look for this single - создаем хранилище данных для андройд
    single {
        DataStoreFactory.create(
            serializer = UserSettingsSerializer,
            // возвращает файл, с которым будет работать новое хранилище данных.
            produceFile = {
                androidContext().dataStoreFile( // see -> SocialApplication comments
                    fileName = PREFERENCES_FILE_NAME
                )
            }
        )
    }
}