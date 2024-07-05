package com.example.socialapp.common.data.remote

import com.example.socialapp.common.data.model.FollowOrUnfollowApiResponse
import com.example.socialapp.common.data.model.FollowsApiResponse
import com.example.socialapp.common.data.model.FollowsParams
import com.example.socialapp.common.util.Constants
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody


// причина по которой этот класс здесь - это мультиплатформенная реализация
// ипользуется в
//home repository || profile repository || followers/following repository
//



internal class FollowsApiService : KtorApi() {
    // отпределим методы взаимодействия с follow api


    suspend fun followUser(
        userToken: String,
        followsParams: FollowsParams
    ): FollowOrUnfollowApiResponse {
        val httpResponse = client.post {
            endPoint(path = "/follows/follow")
            setBody(followsParams)
            setToken(userToken)
        }

        //build follow/unfollow api response
        return FollowOrUnfollowApiResponse(code = httpResponse.status, data = httpResponse.body())
    }

    suspend fun unfollowUser(
        userToken: String,
        followsParams: FollowsParams
    ): FollowOrUnfollowApiResponse {
        val httpResponse = client.post {
            endPoint(path = "/follows/unfollow")
            setBody(followsParams)
            setToken(userToken)
        }

        //build follow/unfollow api response
        return FollowOrUnfollowApiResponse(code = httpResponse.status, data = httpResponse.body())
    }

    suspend fun getFollowableUsers(
        userToken: String,
        userId: Long
    ): FollowsApiResponse {
        val httpResponse = client.get {
            endPoint(path = "/follows/suggestions")
            parameter(key = Constants.USER_ID_PARAMETER, value = userId) // like ?userId=575762586683875328
            setToken(userToken)
        }
        return FollowsApiResponse(code = httpResponse.status, data = httpResponse.body())
    }


}

















