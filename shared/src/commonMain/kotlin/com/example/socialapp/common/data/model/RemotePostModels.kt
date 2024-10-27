package com.example.socialapp.common.data.model

import com.example.socialapp.common.domain.model.Post
import com.example.socialapp.common.util.DateFormatter
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable


@Serializable
internal data class RemotePost(
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
) {
    fun toDomainPost(): Post {
        return Post(
            postId = postId,
            caption = caption,
            imageUrl = imageUrl,
            createdAt = DateFormatter.parseDate(createdAt) ,

            likesCount = likesCount,
            commentsCount = commentsCount,

            userId = userId,
            userName = userName,
            userImageUrl = userImageUrl,

            isLiked = isLiked,
            isOwnPost = isOwnPost
        )

    }


}

@Serializable
internal data class PostsApiResponseData(
    val success: Boolean,
    val posts: List<RemotePost> = listOf(),
    val message: String? = null
)


internal data class PostsApiResponse(
    val code: HttpStatusCode,
    val data: PostsApiResponseData
)


/// запросить один пост
@Serializable
internal data class PostApiResponseData(
    val success: Boolean,
    val post: RemotePost? = null,
    val message: String? = null
)

internal data class PostApiResponse(
    val code: HttpStatusCode,
    val data: PostApiResponseData
)


@Serializable
internal data class NewPostParams(
    val caption: String,
    val userId: Long
)















