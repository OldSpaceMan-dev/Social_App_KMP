package com.example.socialapp.auth.data

import com.example.socialapp.common.data.KtorApi
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

//responsible for communication with our server
internal class AuthService: KtorApi() {

    //take req - return AuthResp
    suspend fun signUp(request: SignUpRequest): AuthResponse = client.post { //client takes from KtorApi
        endPoint(path = "signup")
        setBody(request)
    }.body()

    suspend fun signIn(request: SignInRequest): AuthResponse = client.post {
        endPoint(path = "login")
        setBody(request)
    }.body()

}