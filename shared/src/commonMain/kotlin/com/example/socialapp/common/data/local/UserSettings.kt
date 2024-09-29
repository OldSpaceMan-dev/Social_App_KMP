package com.example.socialapp.common.data.local

import com.example.socialapp.auth.domain.model.AuthResultData
import kotlinx.serialization.Serializable


@Serializable
data class UserSettings(
    val id: Long = -1,
    val name: String = "",
    val bio: String = "",
    val avatar: String? = null,
    val token: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0
)

// need 2 mapping from UserSettings <-> AuthResultData

fun UserSettings.toAuthResultData(): AuthResultData{
    return AuthResultData(
        id, name, bio, avatar, token, followersCount, followingCount
    )
}

//mapper
fun AuthResultData.toUserSettings(): UserSettings {
    return UserSettings(
        id, name, bio, avatar, token, followersCount, followingCount
    )
}

