package com.example.socialapp.auth.domain.usecase

import com.example.socialapp.auth.domain.model.AuthResultData
import com.example.socialapp.auth.domain.repository.AuthRepository
import com.example.socialapp.common.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


//Koin component - can inject our repository directly
class SignInUseCase: KoinComponent {
    private val repository: AuthRepository by inject()

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<AuthResultData> {
        if (email.isBlank() || "@" !in email){
            return Result.Error(
                message = "Invalid email"
            )
        }
        if (password.isBlank() || password.length < 4){
            return Result.Error(
                message = "Invalid password or too short!"
            )
        }

        return repository.signIn( email, password)
    }


}