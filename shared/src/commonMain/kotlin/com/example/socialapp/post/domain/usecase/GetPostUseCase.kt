package com.example.socialapp.post.domain.usecase

import com.example.socialapp.common.domain.model.Post
import com.example.socialapp.common.util.Result
import com.example.socialapp.post.domain.repository.PostRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPostUseCase: KoinComponent {
    private val repository by inject<PostRepository>()

    suspend operator fun invoke(
        postId: Long
    ): Result<Post> {
        return repository.getPost(postId)
    }
}