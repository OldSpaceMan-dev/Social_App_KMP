package com.example.socialapp.follows.domain.usecase

import com.example.socialapp.common.domain.model.FollowsUser
import com.example.socialapp.common.util.Result
import com.example.socialapp.follows.domain.FollowsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


// сделать это как компонент coin, потому
//что напрямую внедриm сюда репозиторий
class GetFollowableUsersUseCase: KoinComponent {
    private val repository by inject<FollowsRepository>()

    suspend operator fun invoke(): Result<List<FollowsUser>> {
        return repository.getFollowableUsers()
    }

}