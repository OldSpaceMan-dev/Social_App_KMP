package com.example.socialapp.post.domain.usecase

import com.example.socialapp.common.domain.model.Post
import com.example.socialapp.common.util.Result
import com.example.socialapp.post.domain.PostRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPostsUseCase: KoinComponent {
    private val repository by inject<PostRepository>()

    suspend operator fun invoke(
        page: Int,
        pageSize: Int
    ): Result<List<Post>> {
        return repository.getFeedPosts(page, pageSize)
    }
}