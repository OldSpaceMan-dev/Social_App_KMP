package com.example.socialapp.android.post

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.account.domain.model.Profile
import com.example.socialapp.android.common.util.Constants
import com.example.socialapp.android.common.util.DefaultPagingManager
import com.example.socialapp.android.common.util.Event
import com.example.socialapp.android.common.util.EventBus
import com.example.socialapp.android.common.util.PagingManager
import com.example.socialapp.common.domain.model.Post
import com.example.socialapp.common.util.Result
import com.example.socialapp.post.domain.model.PostComment
import com.example.socialapp.post.domain.usecase.AddPostCommentUseCase
import com.example.socialapp.post.domain.usecase.GetPostCommentsUseCase
import com.example.socialapp.post.domain.usecase.GetPostUseCase
import com.example.socialapp.post.domain.usecase.LikeOrUnlikePostUseCase
import com.example.socialapp.post.domain.usecase.RemovePostCommentUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val getPostUseCase: GetPostUseCase,
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
    private val likeOrUnlikePostUseCase: LikeOrUnlikePostUseCase,
    private val addPostCommentUseCase: AddPostCommentUseCase,
    private val removePostCommentUseCase: RemovePostCommentUseCase
): ViewModel(){

    var postUiState by mutableStateOf(PostUiState())
        private set

    var commentsUiState by mutableStateOf(CommentsUiState())
        private set

    private lateinit var pagingManager: PagingManager<PostComment>


    init {

        EventBus.events
            .onEach {
                when (it) {
                    is Event.PostUpdated -> updatePost(it.post)
                    is Event.ProfileUpdated -> updateCurrentUserProfileData(it.profile)
                }
            }
            .launchIn(viewModelScope)
    }


    private fun fetchData(postId: Long){ //private тк реализовали onUiAction
        viewModelScope.launch {

            delay(500)
            when (val result = getPostUseCase(postId = postId)) {
                is Result.Success -> {
                    postUiState = postUiState.copy(
                        isLoading = false,
                        post = result.data
                    )

                    fetchPostComments(postId = postId)
                }

                is Result.Error -> {
                    postUiState = postUiState.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }

            }
            /*old realered
            postUiState = postUiState.copy(
                isLoading = true
            )
            commentsUiState = commentsUiState.copy(
                isLoading = true
            )
            //simulate request
            delay(500)

            postUiState = postUiState.copy(
                isLoading = false,
                post = samplePosts.find { it.id == postId }
            )

            commentsUiState = commentsUiState.copy(
                isLoading = false,
                comments = sampleComments
            )
             */
        }
    }


    private suspend fun fetchPostComments(postId: Long) {

        if (commentsUiState.isLoading || commentsUiState.comments.isNotEmpty()) {
            return
        }

        Log.d("PagingManagerComment", " fetchPostComments true")

        //если менежддер пагинации не инициализирован
        if (!::pagingManager.isInitialized) {
            pagingManager = createPagingManager(postId = postId)
        }
        //далее вызывает для инициализации дозагрузки
        pagingManager.loadItems()
    }




    private fun loadMoreComments() {
        Log.d("PagingManagerComment", "loadMoreComments false")
        if (commentsUiState.endReached) return
        Log.d("PagingManagerComment", " loadMoreComments true")
        viewModelScope.launch {
            pagingManager.loadItems()
        }
    }



    private fun createPagingManager(postId: Long): PagingManager<PostComment> {
        return DefaultPagingManager(
            onRequest = {page ->
                getPostCommentsUseCase(
                    postId = postId,
                    page = page,
                    pageSize = Constants.DEFAULT_REQUEST_COMMENTS_PAGE_SIZE
                )
            },
            onSuccess = {comments, _ ->
                Log.d("PagingManagerComment", "createPagingManager Success ")

                //проверка на уникальность? удаляем дубли
                val uniqueComments = (commentsUiState.comments + comments).distinctBy { it.commentId }

                commentsUiState = commentsUiState.copy(
                    //isLoading = false,
                    comments = uniqueComments,//commentsUiState.comments + comments,
                    endReached = comments.size < Constants.DEFAULT_REQUEST_COMMENTS_PAGE_SIZE
                )
            },
            onError = { cause, _ ->
                      commentsUiState = commentsUiState.copy(
                          errorMessage = cause
                      )
            },
            onLoadStateChange = {isLoading ->
                commentsUiState = commentsUiState.copy(
                    isLoading = isLoading
                )
            }
        )
    }


    private fun likeOrDislikePost(post: Post) {
        viewModelScope.launch {
            val count = if (post.isLiked) -1 else +1
            val updatedPost = post.copy(
                isLiked = !post.isLiked,
                likesCount = post.likesCount.plus(count)
            )

            updatePost(updatedPost)

            val result = likeOrUnlikePostUseCase(
                post = post
            )
            when (result) {
                is Result.Error -> {
                    updatePost(post)
                }
                is Result.Success -> {
                    EventBus.send(Event.PostUpdated(updatedPost))
                }
            }

        }
    }

    private fun updatePost(post: Post) {
        postUiState = postUiState.copy(
            post = post
        )
    }

    private fun updateCurrentUserProfileData(profile: Profile) {
        val post = postUiState.post ?: return

        if (post.isOwnPost) {
            val updatedPost = post.copy(
                userName = profile.name,
                userImageUrl = profile.imageUrl
            )
            updatePost(updatedPost) //передаем в функцию новый постUI
        }
        commentsUiState = commentsUiState.copy(
            comments = commentsUiState.comments.map {
                if (it.userId == profile.id) { // owner данного коммента
                    it.copy(
                        userName = profile.name,
                        userImageUrl = profile.imageUrl
                    )
                } else {
                    it
                }
            }
        )

    }



    private fun addNewComment(comment: String) {
        viewModelScope.launch {
            // извлекаем текущую запись из состояния postUiState
            // return@launch возврат из launch корутины -те прерываем корутину а не fun
            val post = postUiState.post ?: return@launch

            commentsUiState = commentsUiState.copy(isAddingNewComment = true)
            delay(500)

            val result = addPostCommentUseCase(
                postId = post.postId,
                content = comment
            )
            Log.d("addNewComment", "true")
            Log.d("addNewCommentPostCommentsRepository", "Result: ${result.message}, ${result.data}")

            when (result) {

                is Result.Error -> {
                    commentsUiState = commentsUiState.copy(
                        errorMessage = result.message,
                        isAddingNewComment = false
                    )
                    Log.d("addNewComment", "false - Error ${commentsUiState.errorMessage}")
                }


                //извлекаем недавно добавленный комментарий из result и обновляем наш список комментариев
                is Result.Success -> {

                    val newComment = result.data ?: return@launch

                    /*
                    //Используйте метод distinctBy для устранения дубликатов
                    val updatedComments = (listOf(newComment) + commentsUiState.comments).distinctBy { it.commentId }
                    commentsUiState = commentsUiState.copy(
                        comments = updatedComments,
                        isAddingNewComment = false
                    )*/

                    val updatedComments = listOf(newComment) + commentsUiState.comments
                    commentsUiState = commentsUiState.copy(
                        comments = updatedComments,
                        isAddingNewComment = false
                    )
                    Log.d("addNewComment", "false -- Comment was been add")

                    //обновляем кол-во комметн +1 и отправояем эвент в EventBus
                    val updatedPost = post.copy(
                        commentsCount = post.commentsCount.plus(1)
                    )
                    EventBus.send(Event.PostUpdated(updatedPost))
                }
            }
        }
    }


    private fun removeComment(postComment: PostComment) {
        viewModelScope.launch {
            val post = postUiState.post ?: return@launch

            val comments = commentsUiState.comments // take comments list - if faild - rollback this list

            // удалили коммет который соответвуют условию  it.commentId == postComment.commentId
            val updatedComments = comments.filter { it.commentId != postComment.commentId }
            commentsUiState = commentsUiState.copy(comments = updatedComments)

            val result = removePostCommentUseCase(
                postId = post.postId,
                commentId = postComment.commentId
            )

            when (result) {

                is Result.Error -> {
                    commentsUiState = commentsUiState.copy(
                        errorMessage = result.message,
                        comments = comments
                    )
                }

                is Result.Success -> {
                    val updatedPost = post.copy(
                        commentsCount = post.commentsCount.minus(other = 1)
                    )
                    EventBus.send(Event.PostUpdated(updatedPost))



                }
            }
        }
    }

    fun onUiAction(uiAction: PostDetailUiAction) {
        when (uiAction) {
            is PostDetailUiAction.FetchPostAction -> fetchData(uiAction.postId)

            is PostDetailUiAction.LoadMoreCommentsAction -> loadMoreComments()

            is PostDetailUiAction.LikeOrDislikePostAction -> likeOrDislikePost(uiAction.post)

            is PostDetailUiAction.AddCommentAction -> addNewComment(uiAction.comment)

            is PostDetailUiAction.RemoveCommentAction -> removeComment(uiAction.postComment)
        }
    }


}


data class PostUiState(
    val isLoading: Boolean = true,
    val post: Post? = null,
    val errorMessage: String? = null
)

data class CommentsUiState(
    val isLoading: Boolean = false,
    val comments: List<PostComment> = listOf(),
    val errorMessage: String? = null,
    val endReached: Boolean = false,
    val isAddingNewComment: Boolean = false
)




sealed interface PostDetailUiAction{
    data class FetchPostAction(val postId: Long): PostDetailUiAction

    data object LoadMoreCommentsAction: PostDetailUiAction

    data class LikeOrDislikePostAction(val post: Post): PostDetailUiAction

    data class AddCommentAction(val comment: String): PostDetailUiAction

    data class RemoveCommentAction(val postComment: PostComment): PostDetailUiAction

}

































