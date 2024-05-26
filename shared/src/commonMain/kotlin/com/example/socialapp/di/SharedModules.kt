package com.example.socialapp.di

import com.example.socialapp.auth.data.AuthRepositoryImpl
import com.example.socialapp.auth.data.AuthService
import com.example.socialapp.auth.domain.repository.AuthRepository
import com.example.socialapp.auth.domain.usecase.SignInUseCase
import com.example.socialapp.auth.domain.usecase.SignUpUseCase
import com.example.socialapp.common.util.provideDispatcher
import org.koin.dsl.module

private val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    //construct installs each time is need
    factory { AuthService() }
    factory { SignUpUseCase() }
    factory { SignInUseCase() }
}

// organize order
private val utilityModule = module {
    factory { provideDispatcher() }
}

//get общие модули
//it going to be called from Android app code
fun getSharedModules() = listOf(authModule, utilityModule)