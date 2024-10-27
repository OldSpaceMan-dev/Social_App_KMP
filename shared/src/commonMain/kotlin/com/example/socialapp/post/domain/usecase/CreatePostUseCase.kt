package com.example.socialapp.post.domain.usecase

import com.example.socialapp.common.domain.model.Post
import com.example.socialapp.common.util.Constants
import com.example.socialapp.common.util.Result
import com.example.socialapp.post.domain.repository.PostRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreatePostUseCase: KoinComponent {

    private val repository: PostRepository by inject()

    suspend operator fun invoke(
        caption: String,
        imageBytes: ByteArray
    ): Result<Post> {
        with(caption) {
            if (isBlank() || length > 20) {
                return Result.Error(message = Constants.INVALID_INPUT_POST_CAPTION_MESSAGE)
            }
        }

        return repository.createPost(caption = caption, imageBytes = imageBytes)
    }


}