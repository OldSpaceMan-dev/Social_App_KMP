package com.example.socialapp.account.data.model

import com.example.socialapp.account.domain.model.Profile
import com.example.socialapp.common.data.local.UserSettings


// convert UserSetting to Profile
fun UserSettings.toDomainProfile() : Profile {
    return Profile(
        id = id,
        name = name,
        bio = bio,
        imageUrl = avatar,
        followersCount = followersCount,
        followingCount = followingCount,
        isFollowing = false,
        isOwnProfile = true,
        postCount = postCount
    )
}


fun Profile.toUserSettings(token: String) : UserSettings {
    return UserSettings(
        id = id,
        name = name,
        bio = bio,
        avatar = imageUrl,
        followersCount = followersCount,
        followingCount = followingCount,
        postCount = postCount,
        token = token
    )
}