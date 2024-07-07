package com.example.socialapp.account.data.repository

import com.example.socialapp.account.data.AccountApiService
import com.example.socialapp.account.data.model.toDomainProfile
import com.example.socialapp.account.data.model.toUserSettings
import com.example.socialapp.account.domain.model.Profile
import com.example.socialapp.account.domain.repository.ProfileRepository
import com.example.socialapp.common.data.local.UserPreferences
import com.example.socialapp.common.util.DispatcherProvider
import com.example.socialapp.common.util.Result
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class ProfileRepositoryImpl(
    private val accountApiService: AccountApiService,
    private val userPreferences: UserPreferences,
    private val dispatcher: DispatcherProvider
) : ProfileRepository {

    override fun getProfile(profileId: Long): Flow<Result<Profile>> {
        return flow {
            val userData = userPreferences.getUserData()

            //check if the requested profile is the current user's profile
            //emit - сообщаем -> success
            if (profileId == userData.id) {
                emit(Result.Success(userData.toDomainProfile()))
            }


            val apiResponse = accountApiService.getProfile(
                token = userData.token,
                profileId = profileId,
                currentUserId = userData.id
            )


            when (apiResponse.code) {

                HttpStatusCode.OK -> {
                    val profile = apiResponse.data.profile!!.toProfile()

                    if (profileId == userData.id) {
                        // if ownProfile авторизуем и отдаем собственные данные
                        userPreferences.setUserData(profile.toUserSettings(userData.token))
                    }
                    emit(Result.Success(profile))
                }
                else -> {
                    emit(Result.Error(message = "Error: ${apiResponse.data.message}"))
                }
            }
        }.catch { exception ->
            emit(Result.Error(message = "Error: ${exception.message}"))
        }.flowOn(dispatcher.io)
    }
}