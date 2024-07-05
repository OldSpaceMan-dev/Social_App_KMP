package com.example.socialapp.follows.domain

import com.example.socialapp.common.domain.model.FollowsUser
import com.example.socialapp.common.util.Result

interface FollowsRepository {

    suspend fun getFollowableUsers(): Result<List<FollowsUser>>

    suspend fun followOrUnfollow(followedUserId: Long, shouldFollow: Boolean): Result<Boolean>
    // followedUserId - id person на которого мы хотим подписаться или не подписываться
}