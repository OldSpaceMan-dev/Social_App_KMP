package com.example.socialapp.auth.data

import com.example.socialapp.auth.domain.model.AuthResultData
import com.example.socialapp.auth.domain.repository.AuthRepository
import com.example.socialapp.common.data.local.UserPreferences
import com.example.socialapp.common.data.local.toUserSettings
import com.example.socialapp.common.util.DispatcherProvider
import com.example.socialapp.common.util.Result
import kotlinx.coroutines.withContext


// нет классов локаль/удаленных источников данных

//что бы эта реализация репозитория отвечала за перенос вызова нашего АПИ в фоновый поток
//и для этого нужен диспатчер


internal class AuthRepositoryImpl(
    private val dispatcher: DispatcherProvider,
    private val authService: AuthService,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData> {
        //Вызывает указанный блок приостановки с заданным контекстом сопрограммы,
        // приостанавливает до тех пор, пока он не завершится, и возвращает результат.
        //-- переключимся на вызов фонового потока
        return withContext(dispatcher.io) {
            try {
                val request = SignUpRequest(name, email, password)

                val authResponse = authService.signUp(request = request)

                if (authResponse.data == null) {
                    Result.Error(
                        //говорим что не null !!
                        message = authResponse.errorMessage!!
                    )
                } else {
                    userPreferences.setUserData(
                        authResponse.data.toAuthResultData().toUserSettings()
                    )
                    Result.Success(
                        data = authResponse.data.toAuthResultData()
                    )
                }
            } catch (e: Exception) {
                Result.Error(
                    message = "Opps, we could not send your request, try later!"
                )
            }
        }
    }


    override suspend fun signIn(
        email: String,
        password: String
    ): Result<AuthResultData> {
        return withContext(dispatcher.io) {
            try {
                val request = SignInRequest(email, password)

                val authResponse = authService.signIn(request = request)

                if (authResponse.data == null) {
                    Result.Error(
                        //говорим что не null !!
                        message = authResponse.errorMessage!!
                    )
                } else {
                    userPreferences.setUserData(
                        authResponse.data.toAuthResultData().toUserSettings()
                    )
                    Result.Success(
                        data = authResponse.data.toAuthResultData()
                    )
                }
            } catch (e: Exception) {
                Result.Error(
                    message = "Opps, we could not send your request, try later!"
                )
            }
        }

    }
}








