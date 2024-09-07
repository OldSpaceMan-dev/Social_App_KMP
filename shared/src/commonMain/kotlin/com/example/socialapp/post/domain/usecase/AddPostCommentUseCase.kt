package com.example.socialapp.post.domain.usecase

import com.example.socialapp.common.util.Result
import com.example.socialapp.post.domain.model.PostComment
import com.example.socialapp.post.domain.repository.PostCommentsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject




class AddPostCommentUseCase : KoinComponent {

    //private val repository by inject<PostCommentsRepository>()
    private val repository: PostCommentsRepository by inject()

    suspend operator fun invoke(
        postId: Long,
        content: String
    ): Result<PostComment> {
        if (content.isBlank()) {
            //Log.d("AddPostCommentUseCase", "Error: content is blank")
            return Result.Error(message = "Comment content can't be empty")
        }
        return repository.addComment(postId = postId, content = content)
    }
}
