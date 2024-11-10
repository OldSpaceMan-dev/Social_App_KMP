package com.example.socialapp.auth.data

import com.example.socialapp.auth.domain.model.AuthResultData


//extension fun to our AuthResponseData
// and need retunt new instance(экземпляр) our AuthResultData

internal fun AuthResponseData.toAuthResultData(): AuthResultData{
    return AuthResultData(
        id, name, bio, avatar, token, followersCount, followingCount, postCount
    )
}