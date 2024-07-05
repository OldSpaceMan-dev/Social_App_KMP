package com.example.socialapp.android.common.fake_data

import com.example.socialapp.common.domain.model.FollowsUser

// NEED FOR ONBORDING PART

data class SampleFollowsUser(
    val id: Int,
    val name: String,
    val bio: String = "Hey there, welcome to my social app page!",
    val profileUrl: String,
    val isFollowing: Boolean = false
){
    fun toFollowsUser(): FollowsUser {
        return FollowsUser(
            id = id.toLong(),
            name = name,
            bio = bio,
            imageUrl = profileUrl,
            isFollowing = isFollowing
        )
    }

}

val sampleUsers = listOf(
    SampleFollowsUser(
        id = 1,
        name = "Mr Smith",
        profileUrl = "https://picsum.photos/200"
    ),
    SampleFollowsUser(
        id = 2,
        name = "John Cena",
        profileUrl = "https://picsum.photos/200"
    ),
    SampleFollowsUser(
        id = 3,
        name = "Cristiano",
        profileUrl = "https://picsum.photos/200"
    ),
    SampleFollowsUser(
        id = 4,
        name = "L. James",
        profileUrl = "https://picsum.photos/200"
    )
)