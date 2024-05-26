package com.example.socialapp.android

import android.app.Application
import com.example.socialapp.android.di.appModule
import com.example.socialapp.di.getSharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SocialApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule + getSharedModules())//implement module from di and ADD to manifest

            // for create androidContext() in single{} in our AppModule and
            // create our dataStoreFile == "app_user_settings"
            androidContext(this@SocialApplication)
        }
    }

}