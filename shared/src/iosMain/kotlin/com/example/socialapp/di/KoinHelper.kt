package com.example.socialapp.di

import com.example.socialapp.account.domain.usecase.GetProfileUseCase
import com.example.socialapp.account.domain.usecase.UpdateProfileUseCase
import com.example.socialapp.auth.domain.usecase.SignInUseCase
import com.example.socialapp.post.domain.usecase.CreatePostUseCase
import com.example.socialapp.post.domain.usecase.GetPostsUseCase
import com.example.socialapp.post.domain.usecase.GetUserPostsUseCase
import com.example.socialapp.post.domain.usecase.LikeOrUnlikePostUseCase
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
    private val updateProfileUseCase by inject<UpdateProfileUseCase>()


    fun signInUseCase() = signInUseCase
    fun getProfileUseCase() = getProfileUseCase
    fun updateProfileUseCase() = updateProfileUseCase


    //Post
    private val getPostsUseCase by inject<GetPostsUseCase>()
    private val getUserPostsUseCase by inject<GetUserPostsUseCase>()
    private val createPostUseCase by inject<CreatePostUseCase>()


    fun getPostsUseCase() = getPostsUseCase
    fun getUserPostsUseCase() = getUserPostsUseCase
    fun createPostUseCase() = createPostUseCase


    //like
    private val likeOrUnlikePostUseCase by inject<LikeOrUnlikePostUseCase>()

    fun likeOrUnlikePostUseCase() = likeOrUnlikePostUseCase

}
















