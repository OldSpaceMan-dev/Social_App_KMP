package com.example.socialapp.common.domain.model



data class Post(
    val postId: Long,
    val caption: String,
    val imageUrl: String,
    val createdAt: String,

    val likesCount: Int,
    val commentsCount: Int,

    val userId: Long,
    val userName: String,
    val userImageUrl: String?,

    val isLiked: Boolean, //current user has like or not
    val isOwnPost: Boolean // current post belong to current user
)