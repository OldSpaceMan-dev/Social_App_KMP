package com.example.socialapp.follows.domain.usecase

import com.example.socialapp.common.util.Result
import com.example.socialapp.follows.domain.FollowsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class FollowOrUnfollowUseCase: KoinComponent {
    private val repository by inject<FollowsRepository>()  //откуда инжектим функцию

    suspend operator fun invoke(
        followedUserId: Long,
        shouldFollow: Boolean
    ): Result<Boolean> {
        return repository.followOrUnfollow(followedUserId, shouldFollow)
    }

}