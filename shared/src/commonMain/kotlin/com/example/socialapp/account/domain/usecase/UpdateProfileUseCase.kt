package com.example.socialapp.account.domain.usecase

import com.example.socialapp.account.domain.model.Profile
import com.example.socialapp.account.domain.repository.ProfileRepository
import com.example.socialapp.common.util.Constants
import com.example.socialapp.common.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class UpdateProfileUseCase: KoinComponent {
    private val repository: ProfileRepository by inject()


    suspend operator fun invoke(
        profile: Profile,
        imageBytes: ByteArray?
    ): Result<Profile> {
        //внутри функции валидируем данные
        with(profile.name) {//получаем строку или блок
            if (isBlank() || length < 3 || length > 20) {
                return Result.Error(message = Constants.INVALID_INPUT_NAME_MESSAGE)
            }
        }

        with(profile.bio) {
            if (isBlank() || length > 150) {
                return Result.Error(message = Constants.INVALID_INPUT_BIO_MESSAGE)
            }
        }

        return repository.updateProfile( profile = profile, imageBytes = imageBytes)

    }

}