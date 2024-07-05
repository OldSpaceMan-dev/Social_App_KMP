package com.example.socialapp.di

import com.example.socialapp.auth.data.AuthRepositoryImpl
import com.example.socialapp.auth.data.AuthService
import com.example.socialapp.auth.domain.repository.AuthRepository
import com.example.socialapp.auth.domain.usecase.SignInUseCase
import com.example.socialapp.auth.domain.usecase.SignUpUseCase
import com.example.socialapp.common.data.remote.FollowsApiService
import com.example.socialapp.common.data.remote.PostApiService
import com.example.socialapp.common.util.provideDispatcher
import com.example.socialapp.follows.data.FollowsRepositoryImpl
import com.example.socialapp.follows.domain.FollowsRepository
import com.example.socialapp.follows.domain.usecase.FollowOrUnfollowUseCase
import com.example.socialapp.follows.domain.usecase.GetFollowableUsersUseCase
import com.example.socialapp.post.data.PostRepositoryImpl
import com.example.socialapp.post.domain.PostRepository
import com.example.socialapp.post.domain.usecase.GetPostsUseCase
import com.example.socialapp.post.domain.usecase.LikeOrUnlikePostUseCase
import org.koin.dsl.module

private val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }

    //construct installs each time is need
    factory { AuthService() }
    factory { SignUpUseCase() }
    factory { SignInUseCase() }
}

// organize order
private val utilityModule = module {
    factory { provideDispatcher() }
}

private val postModule = module {
    factory { PostApiService() }
    factory { GetPostsUseCase() }
    factory { LikeOrUnlikePostUseCase() }

    single<PostRepository> { PostRepositoryImpl(get(), get(), get()) }
}

private val followsModule = module {
    factory { FollowsApiService() }
    factory { FollowOrUnfollowUseCase() }
    factory { GetFollowableUsersUseCase() }

    single<FollowsRepository> { FollowsRepositoryImpl(get(), get(), get()) }


}


//get общие модули
//it going to be called from Android app code
fun getSharedModules() = listOf(
    platformModule,
    authModule,
    utilityModule,
    postModule,
    followsModule
)