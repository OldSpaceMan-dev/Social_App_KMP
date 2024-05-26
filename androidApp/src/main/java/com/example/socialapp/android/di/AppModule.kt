package com.example.socialapp.android.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.socialapp.android.MainActivityViewModel
import com.example.socialapp.android.account.edit.EditProfileViewModel
import com.example.socialapp.android.account.follows.FollowsViewModel
import com.example.socialapp.android.account.profile.ProfileViewModel
import com.example.socialapp.android.auth.login.LoginViewModel
import com.example.socialapp.android.auth.singup.SignUpViewModel
import com.example.socialapp.android.common.datastore.UserSettingsSerializer
import com.example.socialapp.android.home.HomeScreenViewModel
import com.example.socialapp.android.post.PostDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module { //koin
    //injects
    viewModel { LoginViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { MainActivityViewModel(get()) }
    viewModel { HomeScreenViewModel() }
    viewModel { PostDetailViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { EditProfileViewModel() }
    viewModel { FollowsViewModel() }

    // get look for this single
    single {
        DataStoreFactory.create(
            serializer = UserSettingsSerializer,
            // возвращает файл, с которым будет работать новое хранилище данных.
            produceFile = {
                androidContext().dataStoreFile( // see -> SocialApplication comments
                    fileName = "app_user_settings"
                )
            }
        )
    }
}

