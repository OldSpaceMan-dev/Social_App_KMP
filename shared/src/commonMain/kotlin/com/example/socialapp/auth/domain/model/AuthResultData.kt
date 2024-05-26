package com.example.socialapp.auth.domain.model

//AuthResultData - domain layer model ?? that we get from our server matching (AuthResponseData)

data class AuthResultData(
    val id: Long,
    val name: String,
    val bio: String,
    val avatar: String? = null,
    val token: String,
    val followersCount: Int = 0,
    val followingCount: Int = 0
)


//need MAP from AuthResponseData (in Model) for AuthResultData