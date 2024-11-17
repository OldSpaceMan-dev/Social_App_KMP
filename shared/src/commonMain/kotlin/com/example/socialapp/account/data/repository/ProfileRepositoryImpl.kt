package com.example.socialapp.account.data.repository

import com.example.socialapp.account.data.AccountApiService
import com.example.socialapp.account.data.model.UpdateUserParams
import com.example.socialapp.account.data.model.toDomainProfile
import com.example.socialapp.account.data.model.toUserSettings
import com.example.socialapp.account.domain.model.Profile
import com.example.socialapp.account.domain.repository.ProfileRepository
import com.example.socialapp.common.data.local.UserPreferences
import com.example.socialapp.common.util.DispatcherProvider
import com.example.socialapp.common.util.Result
import com.example.socialapp.common.util.safeApiCall
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

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



    override suspend fun updateProfile(
        profile: Profile,
        imageBytes: ByteArray?
    ): Result<Profile> {
        //убедиться, что наши запросы к api завершаются правильной ошибкой
        return safeApiCall(dispatcher) {
            val currentUserData = userPreferences.getUserData() // также есть токен внутри юзердаты

            //преобразем данные в json строку - отправляемую на сервер
            val profileData = Json.encodeToString( //преобразует данные которые понятные беку
                serializer = UpdateUserParams.serializer(), // тк дата класс сериалайз
                value = UpdateUserParams( //отправляем на сервер лок данные
                    userId = profile.id,
                    name = profile.name,
                    bio = profile.bio,
                    imageUrl = profile.imageUrl
                )
            )

            //ари реквест на обновление данных пользователя
            val apiResponse = accountApiService.updateProfile(
                token = currentUserData.token,
                profileData = profileData,
                imageBytes = imageBytes
            )

            if (apiResponse.code == HttpStatusCode.OK) {
                var imageUrl = profile.imageUrl
                if (imageUrl != null) { // если image загружен

                    //получаем последние данные пользователя
                    // НЕ оптимально - тк бек должен возвращать данные в том же апи запросе
                    // и пока это не так мы обновляем их здесь - когда статус апри ОК
                    val updatedProfileApiResponse = accountApiService.getProfile(
                        token = currentUserData.token,
                        profileId = profile.id,
                        currentUserId = profile.id
                    )
                    //после получ данных - обновляем данные image url
                    updatedProfileApiResponse.data.profile?.let {
                        imageUrl = it.imageUrl
                    }
                }

                //обновление и маппинг данных - если image был загружен
                val updateProfile = profile.copy(imageUrl = imageUrl)

                userPreferences.setUserData(
                    // и обновялем их для сериалайз данных юзерСеттинз
                    userSettings = updateProfile.toUserSettings(currentUserData.token)
                )

                Result.Success(data = updateProfile)
            } else {
                Result.Error(message = "ApiError: ${apiResponse.data.message}")
            }

        }

    }

}





































