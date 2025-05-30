package com.example.socialapp.android.common.fake_data

import com.example.socialapp.common.domain.model.Post

data class Profile(
    val id: Int,
    val name: String,
    val bio: String,
    val profileUrl: String,
    val followersCount: Int,
    val followingCount: Int,
    val isOwnProfile: Boolean = false,
    val isFollowing: Boolean = false,
    val postCount: Int = 0
) {
    fun toDomainProfiel(): com.example.socialapp.account.domain.model.Profile {
        return com.example.socialapp.account.domain.model.Profile(
            id = id.toLong(),
            name = name,
            bio = bio,
            imageUrl = profileUrl,
            followersCount = followersCount,
            followingCount = followingCount,
            isFollowing = isFollowing,
            isOwnProfile = isOwnProfile,
            postCount = postCount
        )
    }
}


val sampleProfiles = listOf(
    Profile(
        id = 1,
        name = "Mr Smith",
        bio = "Hey there, welcome to my Social App page!",
        profileUrl = "https://picsum.photos/200",
        followersCount = 23,
        followingCount = 13,
        isOwnProfile = true,
        isFollowing = true
    ),

    Profile(
        id = 2,
        name = "John Cena",
        profileUrl = "https://picsum.photos/200",
        bio = "Hey there, welcome to my Social App page!",
        followersCount = 23,
        followingCount = 13,
        isOwnProfile = true,
        isFollowing = true
    ),

    Profile(
        id = 3,
        name = "Cristiano",
        profileUrl = "https://picsum.photos/200",
        bio = "Hey there, welcome to my Social App page!",
        followersCount = 23,
        followingCount = 13,
        isOwnProfile = true,
        isFollowing = true
    ),

    Profile(
        id = 4,
        name = "L. James",
        profileUrl = "https://picsum.photos/200",
        bio = "Hey there, welcome to my Social App page!",
        followersCount = 23,
        followingCount = 13,
        isOwnProfile = true,
        isFollowing = true
    )
)