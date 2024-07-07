package com.example.socialapp.account.domain.usecase

import com.example.socialapp.account.domain.model.Profile
import com.example.socialapp.account.domain.repository.ProfileRepository
import com.example.socialapp.common.util.Result
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class GetProfileUseCase : KoinComponent {
    private val repository: ProfileRepository by inject()
    // inject allow us call GetProfileUseCase - как если бы она была функцией

    operator fun invoke(profileId: Long): Flow<Result<Profile>> {
        return repository.getProfile(profileId = profileId)
    }

}