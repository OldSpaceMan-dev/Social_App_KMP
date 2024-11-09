package com.example.socialapp.account.data.model

import com.example.socialapp.account.domain.model.Profile
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable


@Serializable
internal data class RemoteProfile(
    val id: Long,
    val name: String,
    val bio: String,
    val imageUrl: String?,

    val followersCount: Int,
    val followingCount: Int,

    val isFollowing: Boolean,
    val isOwnProfile: Boolean,

    //var postCount: Int = 0
) {
    fun toProfile() : Profile { //convert RemoteProfile -> Profile
        return Profile(
            id,
            name,
            bio,
            imageUrl,
            followersCount,
            followingCount,
            isFollowing,
            isOwnProfile,
            //postCount = postCount
        )
    }

}

@Serializable
data class UpdateUserParams(
    val userId: Long,
    val name: String,
    val bio: String,
    val imageUrl: String? = null
)




@Serializable
internal data class ProfileApiResponseData(
    val success: Boolean,
    val profile: RemoteProfile?,
    val message: String? = null
)

internal data class ProfileApiResponse(
    val code: HttpStatusCode,
    val data: ProfileApiResponseData
)