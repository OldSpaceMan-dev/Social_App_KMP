package com.example.socialapp.android.account.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.account.domain.model.Profile
import com.example.socialapp.account.domain.usecase.GetProfileUseCase

import com.example.socialapp.android.common.fake_data.samplePosts
import com.example.socialapp.android.common.fake_data.sampleProfiles
import com.example.socialapp.android.common.util.Constants
import com.example.socialapp.android.common.util.DefaultPagingManager
import com.example.socialapp.android.common.util.Event
import com.example.socialapp.android.common.util.EventBus
import com.example.socialapp.android.common.util.PagingManager
import com.example.socialapp.common.domain.model.Post
import com.example.socialapp.common.util.Result
import com.example.socialapp.follows.domain.usecase.FollowOrUnfollowUseCase
import com.example.socialapp.post.domain.usecase.GetUserPostsUseCase
import com.example.socialapp.post.domain.usecase.LikeOrUnlikePostUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val followOrUnfollowUseCase: FollowOrUnfollowUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val likeOrUnlikePostUseCase: LikeOrUnlikePostUseCase
): ViewModel(){

    var userInfoUiState by mutableStateOf(UserInfoUiState())
        private set

    var profilePostsUiState by mutableStateOf(ProfilePostsUiState())
        private set

    //будет инициализирована позже.
    private lateinit var pagingManager: PagingManager<Post>


    private fun fetchProfile(userId: Long){
        viewModelScope.launch {

            getProfileUseCase(profileId = userId)
                .onEach {
                    when(it){
                        is Result.Error -> {
                            userInfoUiState = userInfoUiState.copy(
                                isLoading = false,
                                errorMessage = it.message
                            )
                        }
                        is Result.Success -> {
                            userInfoUiState = userInfoUiState.copy(
                                isLoading = false,
                                profile = it.data
                            )
                            fetchProfilePosts(profileId = userId)
                        }
                    }
                }.collect()
            /* old realisation
            delay(1000) // simulate net work request

            userInfoUiState = userInfoUiState.copy(
                isLoading = false,
                profile = sampleProfiles.find { it.id == userId } //find same id
            )

            profilePostsUiState = profilePostsUiState.copy(
                isLoading = false,
                posts = samplePosts
            )
             */
        }
    }

    private fun followUser(profile: Profile){
        viewModelScope.launch {

            val count = if (profile.isFollowing) -1 else +1
            userInfoUiState = userInfoUiState.copy(
                profile = userInfoUiState.profile?.copy(
                    isFollowing = !profile.isFollowing,
                    followersCount = profile.followersCount.plus(count)
                )
            )

            val result = followOrUnfollowUseCase(
                followedUserId = profile.id,
                shouldFollow = !profile.isFollowing
            )

            when(result){
                is Result.Error -> {
                    userInfoUiState = userInfoUiState.copy(
                        profile = userInfoUiState.profile?.copy(
                            isFollowing = profile.isFollowing,
                            followersCount = profile.followersCount
                        )
                    )
                }
                is Result.Success -> Unit // так как в начале функции мы уже обновили UI
            }
        }
    }


    private suspend fun fetchProfilePosts(profileId: Long) {
        if (profilePostsUiState.isLoading || profilePostsUiState.posts.isNotEmpty()) return

        // проверяю инициализацию метода pagingManager, если нет то выполняю инициализацию
        if (!::pagingManager.isInitialized){ // is Not Initialized (! in start)
            pagingManager = createPagingManager(profileId = profileId)
        }

        pagingManager.loadItems()
    }

    private fun createPagingManager(profileId: Long): PagingManager<Post> {
        return DefaultPagingManager(
            onRequest = {page ->
                getUserPostsUseCase(
                    userId = profileId,
                    page = page,
                    pageSize = Constants.DEFAULT_REQUEST_PAGE_SIZE
                )
            },
            onSuccess = {posts, _ ->
                profilePostsUiState = if (posts.isEmpty()) {
                     profilePostsUiState.copy(
                         endReached = true
                     )
                } else {
                     profilePostsUiState.copy(
                         posts = profilePostsUiState.posts + posts,
                         endReached = posts.size < Constants.DEFAULT_REQUEST_PAGE_SIZE
                     )
                }
            },
            onError = {message, _ ->

                profilePostsUiState = profilePostsUiState.copy(
                     errorMessage = message
                )
            },
            onLoadStateChange = {isLoading ->
                profilePostsUiState = profilePostsUiState.copy(isLoading = isLoading)
            }
        )
    }

    private fun loadMorePosts() {
        if (profilePostsUiState.endReached) return // достигли конца - ничего не делаем
        viewModelScope.launch { pagingManager.loadItems() } // если нет - запускаем пагинацию
    }



    private fun likeOrDislikePost(post: Post) {
        viewModelScope.launch{
            val count = if (post.isLiked) -1 else +1

            val updatedProfilePost = post.copy(
                isLiked = !post.isLiked,
                likesCount = post.likesCount.plus(count)
            )

            updatePost(updatedProfilePost)
            /* same old update
            profilePostsUiState = profilePostsUiState.copy(
                posts = profilePostsUiState.posts.map {
                    if (it.postId == post.postId) updatedPost else it
                }
            )
            */

            val result = likeOrUnlikePostUseCase(
                post = post,
            )

            when(result){
                is Result.Error -> {

                    updatePost(post)
                    /*
                    profilePostsUiState = profilePostsUiState.copy(
                        posts = profilePostsUiState.posts.map {
                            if (it.postId == post.postId) post else it
                        }
                    )
                     */
                }
                is Result.Success -> {//Unit // уже обновили UI выше
                    EventBus.send(Event.PostUpdated(updatedProfilePost))
                }

            }
        }
    }


    private fun updatePost(post: Post) {
        profilePostsUiState = profilePostsUiState.copy(
            posts = profilePostsUiState.posts.map {
                if (it.postId == post.postId) post else it
            }
        )
    }



    fun onUiAction(uiAction: ProfileUiAction) {
        when(uiAction) {
            is ProfileUiAction.FetchProfileAction -> fetchProfile(uiAction.profileId)
            is ProfileUiAction.FollowUserAction -> followUser(uiAction.profile)
            is ProfileUiAction.LoadMorePostsAction -> loadMorePosts()
            is ProfileUiAction.PostLikeAction -> likeOrDislikePost(uiAction.post)
        }
    }


}




data class UserInfoUiState(
    val isLoading: Boolean = true, // when we open -> show progress bar
    val profile: Profile? = null,
    val errorMessage: String? = null
)

data class ProfilePostsUiState(
    val isLoading: Boolean = false, // // when we open -> show progress bar
    val posts: List<Post> = listOf(),
    val errorMessage: String? = null,
    val endReached: Boolean = false
)


sealed interface ProfileUiAction {

    data class FetchProfileAction(val profileId: Long) : ProfileUiAction

    data class FollowUserAction(val profile: Profile) : ProfileUiAction

    data object LoadMorePostsAction: ProfileUiAction

    data class PostLikeAction(val post: Post) : ProfileUiAction
}



























