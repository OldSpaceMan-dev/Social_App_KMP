package com.example.socialapp.di

import com.example.socialapp.account.domain.usecase.GetProfileUseCase
import com.example.socialapp.auth.domain.usecase.SignInUseCase
import com.example.socialapp.post.domain.usecase.GetPostsUseCase
import com.example.socialapp.post.domain.usecase.GetUserPostsUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import kotlin.experimental.ExperimentalObjCName

//@OptIn(ExperimentalObjCName::class)
//@ObjCName(name = "KoinHelperKt")
fun initKoin() {


    startKoin {
        modules(getSharedModules())
    }
}

class KoinIOSHelper: KoinComponent {

    //Account
    private val signInUseCase by inject<SignInUseCase>()
    private val getProfileUseCase by inject<GetProfileUseCase>()


    fun signInUseCase() = signInUseCase
    fun getProfileUseCase() = getProfileUseCase


    //Post
    private val getPostsUseCase by inject<GetPostsUseCase>()
    private val getUserPostsUseCase by inject<GetUserPostsUseCase>()

    fun getPostsUseCase() = getPostsUseCase
    fun getUserPostsUseCase() = getUserPostsUseCase

}
