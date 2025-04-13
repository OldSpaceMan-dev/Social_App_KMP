package com.example.socialapp.android.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.socialapp.android.MainActivityViewModel
import com.example.socialapp.android.account.edit.EditProfileViewModel
import com.example.socialapp.android.account.follows.FollowsViewModel
import com.example.socialapp.android.account.profile.ProfileViewModel
import com.example.socialapp.android.auth.login.LoginViewModel
import com.example.socialapp.android.auth.singup.SignUpViewModel
import com.example.socialapp.android.common.util.CacheManager
import com.example.socialapp.android.common.util.ImageBytesReader
import com.example.socialapp.android.home.HomeScreenViewModel
import com.example.socialapp.android.post.PostDetailViewModel
import com.example.socialapp.android.post.create_post.CreatePostViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module { //koin
    //injects
    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { MainActivityViewModel(get()) }
    viewModel { HomeScreenViewModel(get(), get(), get(), get()) }
    viewModel { PostDetailViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { EditProfileViewModel(get(), get(), get(),  get(), get()) }
    viewModel { FollowsViewModel(get()) }

    //для загрузки изображения
    single { ImageBytesReader(androidContext()) }
    viewModel { CreatePostViewModel(get(), get()) }


    // Utility classes
    single { CacheManager(androidContext()) } // для очистки кеша

}

