package com.example.socialapp.common.domain.model

data class FollowsUser(
    val id: Long,
    val name: String,
    val bio: String,
    val imageUrl: String? = null,
    val isFollowing: Boolean
)