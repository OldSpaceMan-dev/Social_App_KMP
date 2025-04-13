package com.example.socialapp.di

import com.example.socialapp.account.data.AccountApiService
import com.example.socialapp.account.data.repository.ProfileRepositoryImpl
import com.example.socialapp.account.domain.repository.ProfileRepository
import com.example.socialapp.account.domain.usecase.GetProfileUseCase
import com.example.socialapp.account.domain.usecase.UpdateProfileUseCase
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
import com.example.socialapp.follows.domain.usecase.GetFollowsUseCase
import com.example.socialapp.post.data.remote.PostCommentsApiService
import com.example.socialapp.post.data.repository.PostCommentsRepositoryImpl
import com.example.socialapp.post.data.repository.PostRepositoryImpl
import com.example.socialapp.post.domain.repository.PostCommentsRepository
import com.example.socialapp.post.domain.repository.PostRepository
import com.example.socialapp.post.domain.usecase.AddPostCommentUseCase
import com.example.socialapp.post.domain.usecase.CreatePostUseCase
import com.example.socialapp.post.domain.usecase.GetPostCommentsUseCase
import com.example.socialapp.post.domain.usecase.GetPostUseCase
import com.example.socialapp.post.domain.usecase.GetPostsUseCase
import com.example.socialapp.post.domain.usecase.GetUserPostsUseCase
import com.example.socialapp.post.domain.usecase.LikeOrUnlikePostUseCase
import com.example.socialapp.post.domain.usecase.RemovePostCommentUseCase
import com.example.socialapp.post.domain.usecase.RemovePostUseCase
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
    factory { GetUserPostsUseCase() }
    factory { GetPostUseCase() }
    factory { CreatePostUseCase() }
    factory { RemovePostUseCase() }

    single<PostRepository> { PostRepositoryImpl(get(), get(), get()) }
}

private val postCommentModule = module {
    factory { PostCommentsApiService() }
    factory { GetPostCommentsUseCase() }
    factory { AddPostCommentUseCase() }
    factory { RemovePostCommentUseCase() }

    single<PostCommentsRepository> { PostCommentsRepositoryImpl(get(), get(), get()) }
}


private val followsModule = module {
    factory { FollowsApiService() }
    factory { FollowOrUnfollowUseCase() }
    factory { GetFollowableUsersUseCase() }
    factory { GetFollowsUseCase() }

    single<FollowsRepository> { FollowsRepositoryImpl(get(), get(), get()) }
}

private val accountModule = module {
    factory { AccountApiService() }
    factory { GetProfileUseCase() }
    factory { UpdateProfileUseCase() }

    single<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get()) }
}


//get общие модули
//it going to be called from Android app code
fun getSharedModules() = listOf(
    platformModule,
    authModule,
    utilityModule,
    postModule,
    postCommentModule,
    followsModule,
    accountModule
)


/*
private val settingsModule = module {

}

val settingsModule = DI.Module("settings") {
    bind<Settings>() with singleton {
        SettingsFactory().createSettings(
            configuration = instance(),
            name = "customer_sdk_settings",
        )
    }
 */












