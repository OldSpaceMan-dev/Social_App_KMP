package com.example.socialapp.post.domain.usecase

import com.example.socialapp.common.domain.model.Post
import com.example.socialapp.common.util.Result
import com.example.socialapp.post.domain.PostRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetUserPostsUseCase : KoinComponent {
    private val repository by inject<PostRepository>()
    // inject allow us call GetUserPostsUseCase - как если бы она была функцией

    suspend operator fun invoke(
        userId: Long,
        page: Int,
        pageSize: Int
    ): Result<List<Post>> {
        return repository.getUserPosts(userId = userId, page = page, pageSize = pageSize)
    }

}