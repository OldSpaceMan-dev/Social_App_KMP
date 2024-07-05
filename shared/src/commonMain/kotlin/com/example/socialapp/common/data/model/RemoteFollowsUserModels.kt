package com.example.socialapp.common.data.model

import com.example.socialapp.common.domain.model.FollowsUser
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable


@Serializable
internal data class FollowsParams(
    val follower: Long, // id user - кто подписывается
    val following: Long // на кого подписываемся
)

@Serializable
internal data class RemoteFollowUser(
    val id: Long,
    val name: String,
    val bio: String,
    val imageUrl: String? = null,
    val isFollowing: Boolean
) {
    //method to convert remote -> domain follow user
    fun toDomainFollowUser(): FollowsUser {
        return FollowsUser(id, name, bio, imageUrl, isFollowing)
    }
}


//ответ от сервера
@Serializable
internal data class FollowsApiResponseData(
    val success: Boolean,
    val follows: List<RemoteFollowUser> = listOf(), //list of follower/following
    val message: String? = null
)

//результат от сервера
internal data class FollowsApiResponse(
    val code: HttpStatusCode,  // различный код для follower/ unfollower user
    val data: FollowsApiResponseData
)


//when we send request to follower/ unfollower user
@Serializable
internal data class FollowOrUnfollowResponseData(
    val success: Boolean,
    val message: String? = null
)

internal data class FollowOrUnfollowApiResponse(
    val code: HttpStatusCode,  // различный код для follower/ unfollower user
    val data: FollowOrUnfollowResponseData
)












