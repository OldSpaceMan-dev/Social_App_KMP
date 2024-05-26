package com.example.socialapp.auth.domain.repository

import com.example.socialapp.auth.domain.model.AuthResultData
import com.example.socialapp.common.util.Result

internal interface AuthRepository {

    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData> // it like return

    suspend fun signIn(
        email: String,
        password: String
    ): Result<AuthResultData> // it like return

}

