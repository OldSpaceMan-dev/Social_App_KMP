package com.example.socialapp.auth.domain.usecase

import com.example.socialapp.auth.domain.model.AuthResultData
import com.example.socialapp.auth.domain.repository.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.example.socialapp.common.util.Result


//Koin component - can inject our repository directly
class SignUpUseCase: KoinComponent{
    private val repository: AuthRepository by inject()

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData>{
        // add basic validation for Name / Email / Passw

        if (name.isBlank() || name.length < 3) { // менее трех символов
            return Result.Error(
                message = "Invalid name"
            )
        }
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

        return repository.signUp(name, email, password)
    }


}