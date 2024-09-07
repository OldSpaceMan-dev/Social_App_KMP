package com.example.socialapp.post.domain.repository

import com.example.socialapp.common.util.Result
import com.example.socialapp.post.domain.model.PostComment


internal interface PostCommentsRepository {

    suspend fun getPostComments(postId: Long, page: Int, pageSize: Int): Result<List<PostComment>>

    suspend fun addComment(postId: Long, content: String): Result<PostComment>

    suspend fun removeComment(postId: Long, commentId: Long): Result<PostComment?>

}