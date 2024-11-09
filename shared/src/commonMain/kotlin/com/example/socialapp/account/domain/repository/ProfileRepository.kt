package com.example.socialapp.account.domain.repository

import com.example.socialapp.account.domain.model.Profile
import com.example.socialapp.common.util.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getProfile(profileId: Long): Flow<Result<Profile>>

    suspend fun updateProfile(profile: Profile, imageBytes: ByteArray?): Result<Profile>

    // Новый метод для получения профиля с количеством постов
    //fun getProfileWithPostCount(profileId: Long): Flow<Result<Profile>>

}