package com.example.socialapp.android.common.fake_data

// NEED FOR ONBORDING PART

data class FollowsUser(
    val id: Int,
    val name: String,
    val bio: String = "Hey there, welcome to my social app page!",
    val profileUrl: String,
    val isFollowing: Boolean = false
)

val sampleUsers = listOf(
    FollowsUser(
        id = 1,
        name = "Mr Smith",
        profileUrl = "https://picsum.photos/200"
    ),
    FollowsUser(
        id = 2,
        name = "John Cena",
        profileUrl = "https://picsum.photos/200"
    ),
    FollowsUser(
        id = 3,
        name = "Cristiano",
        profileUrl = "https://picsum.photos/200"
    ),
    FollowsUser(
        id = 4,
        name = "L. James",
        profileUrl = "https://picsum.photos/200"
    )
)