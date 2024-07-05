package com.example.socialapp.di

import com.example.socialapp.common.data.IOSUserPreferences
import com.example.socialapp.common.data.createDatastore
import com.example.socialapp.common.data.local.UserPreferences
import org.koin.dsl.module


actual val platformModule = module {

    single<UserPreferences> { IOSUserPreferences(get()) }

    //пользовательские найстроки айос также зависят от хранилищя данных здесь

    // создаем хранилище данных
    single {
        createDatastore()
    }

}