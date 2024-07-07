package com.example.socialapp.account.domain.repository

import com.example.socialapp.account.domain.model.Profile
import com.example.socialapp.common.util.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfile(profileId: Long): Flow<Result<Profile>>

}