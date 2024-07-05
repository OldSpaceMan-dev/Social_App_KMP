package com.example.socialapp.android.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.android.common.util.Constants
import com.example.socialapp.android.common.util.DefaultPagingManager
import com.example.socialapp.android.common.util.PagingManager
import com.example.socialapp.common.domain.model.FollowsUser
import com.example.socialapp.common.domain.model.Post
import com.example.socialapp.common.util.Result
import com.example.socialapp.follows.domain.usecase.FollowOrUnfollowUseCase
import com.example.socialapp.follows.domain.usecase.GetFollowableUsersUseCase
import com.example.socialapp.post.domain.usecase.GetPostsUseCase
import com.example.socialapp.post.domain.usecase.LikeOrUnlikePostUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val getFollowableUsersUseCase: GetFollowableUsersUseCase,
    private val followOrUnfollowUseCase: FollowOrUnfollowUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val likePostUseCase: LikeOrUnlikePostUseCase
): ViewModel() {

    var postsFeedUiState by mutableStateOf(PostsFeedUiState())
        private set

    var onBoardingUiState by mutableStateOf(OnBoardingUiState())
        private set

    var homeRefreshState by mutableStateOf(HomeRefreshState())
        private set


    private val pagingManager by lazy { createPagingManager() }

    init {
        fetchData()
    }


    private fun fetchData(){
        homeRefreshState = homeRefreshState.copy(isRefreshing = true)

        //извлекать данные
        //onBoardingUiState = onBoardingUiState.copy(isLoading = true)
        //postsFeedUiState = postsFeedUiState.copy(isLoading = true)

        viewModelScope.launch {
            delay(1000)


            //fetch real suggestion date
            // сделаем ассинронную загрузку онбординга "deferred - отложено"
            val onboardingDeferred = async {
                getFollowableUsersUseCase()
            } // вставим ниже
            //val users = getFollowableUsersUseCase()


            /* use useCase
             onBoardingUiState = onBoardingUiState.copy(
                 isLoading = false,
                 users = sampleUsers,
                 shouldShowOnBoarding = true
             )
              */

            /* firs pagination realization
            postsFeedUiState = postsFeedUiState.copy(
                isLoading = false,
                posts = samplePosts,

            )
            */

            pagingManager.apply {
                reset()
                loadItems() // wait when getFollowableUsersUseCase - gate result
            }

            handleOnBoardingResult(onboardingDeferred.await())

            //stop refresh
            homeRefreshState = homeRefreshState.copy(isRefreshing = false)
        }
    }


    private fun createPagingManager(): PagingManager<Post> {
        return DefaultPagingManager(
            onRequest = {page ->
                getPostsUseCase(page, Constants.DEFAULT_REQUEST_PAGE_SIZE)
            },
            onSuccess = {posts, page ->
                postsFeedUiState = if (posts.isEmpty()) {
                    postsFeedUiState.copy(endReached = true)
                }else{
                    if (page == Constants.INITIAL_PAGE_NUMBER) {
                        // сбрасываем список постов что бы потом подрузить новые (рефреш\старт загрузки)
                        postsFeedUiState = postsFeedUiState.copy(posts = emptyList())
                    }
                    postsFeedUiState.copy(
                        posts = postsFeedUiState.posts + posts,
                        endReached = posts.size < Constants.DEFAULT_REQUEST_PAGE_SIZE
                    )
                }
            },
            onError = {cause, page ->
                //refresh has failed
                if (page == Constants.INITIAL_PAGE_NUMBER){
                    homeRefreshState = homeRefreshState.copy(
                        refreshErrorMessage = cause
                    )
                }else{ // if is not initial page
                    postsFeedUiState = postsFeedUiState.copy(
                        loadingErrorMessage = cause
                    )
                }
            },
            onLoadStateChange = {isLoading ->
                postsFeedUiState = postsFeedUiState.copy(
                    isLoading = isLoading
                )
            }
        )
    }


    private fun loadMorePosts() {
        if (postsFeedUiState.endReached) return
        viewModelScope.launch {
            pagingManager.loadItems()
        }
    }


    private fun dismissOnboarding() {
        //узнать подписаны ли на какого нибудь пользователя
        val hasFollowing = onBoardingUiState.followableUsers.any { it.isFollowing }

        if (!hasFollowing) {
            //TODO tell the user they need to follow at least one person
            //create message to show
        }else {
            onBoardingUiState =
                onBoardingUiState.copy(shouldShowOnBoarding = false, followableUsers = emptyList())
            fetchData()
        }

    }


    private fun handleOnBoardingResult(result: Result<List<FollowsUser>>) {
        when (result) {
            is Result.Error -> Unit

            is Result.Success -> {
                result.data?.let {followsUsers ->
                    onBoardingUiState = onBoardingUiState.copy(
                        shouldShowOnBoarding = followsUsers.isNotEmpty(),
                        followableUsers = followsUsers
                    )
                }
            }
        }
    }

    private fun followUser(followsUser: FollowsUser) {
        viewModelScope.launch {
            val result = followOrUnfollowUseCase(
                followedUserId = followsUser.id,
                shouldFollow = !followsUser.isFollowing
            )

            //change UI State of onbording
            onBoardingUiState = onBoardingUiState.copy(
                followableUsers = onBoardingUiState.followableUsers.map {
                    if (it.id == followsUser.id) { // текущий айди = айди за которым след
                        // идем по списку пользователей и проверяем, равны их айди (сам на себя )
                        // если да (я=я и не подписан на себя)
                        // -> isFollowing = true и мы его пропускаем shouldFollow = false

                        // Если текущий пользователь имеет тот же id, что и followsUser:
                        // Создаем копию пользователя с измененным значением isFollowing.
                        it.copy(isFollowing = !followsUser.isFollowing)
                    } else it
                }
            )
            when (result) {
                //ошибка -> сбросим
                //состояние пользовательского интерфейса при подключении и снова включим следующее
                is Result.Error -> {
                    onBoardingUiState = onBoardingUiState.copy(
                        followableUsers = onBoardingUiState.followableUsers.map {
                            if (it.id == followsUser.id) {
                                it.copy(isFollowing = followsUser.isFollowing)
                            } else it
                        }
                    )
                }
                is Result.Success -> Unit
            }
        }
    }

    private fun likeOrUnlikePost(post: Post) {
        viewModelScope.launch {
            val count = if (post.isLiked) -1 else +1
            postsFeedUiState = postsFeedUiState.copy(
                posts = postsFeedUiState.posts.map {
                    if (it.postId == post.postId) {
                        it.copy(
                            isLiked = !post.isLiked, // if false -> true and plus +1
                            likesCount = post.likesCount.plus(count)
                        )
                    } else it
                }
            )

            val result = likePostUseCase(
                post = post
            )

            when (result) {
                is Result.Error -> {
                    postsFeedUiState = postsFeedUiState.copy(
                        posts = postsFeedUiState.posts.map {
                            if (it.postId == post.postId) post else it
                        }
                    )
                }

                is Result.Success -> Unit
            }
        }
    }



    /*
    is используется для типов с параметрами или дополнительными данными ---- для проверки вхлждения
    в остальном все просто перечисление и не нужно проверять
    HomeUiAction.FollowUserAction и HomeUiAction.PostLikeAction содержат дополнительные данные
(например, user в FollowUserAction).
    Оператор is проверяет, является ли uiAction экземпляром определенного подкласса HomeUiAction,
и если да, то позволяет получить доступ к свойствам этого подкласса.

     */
    fun onUiAction(uiAction: HomeUiAction) {
        when (uiAction) {
            is HomeUiAction.FollowUserAction -> followUser(uiAction.user)
            HomeUiAction.LoadMorePostsAction -> loadMorePosts()
            is HomeUiAction.PostLikeAction -> likeOrUnlikePost(uiAction.post)
            HomeUiAction.RefreshAction -> fetchData()
            HomeUiAction.RemoveOnboardingAction -> dismissOnboarding()

        }
    }


}


data class HomeRefreshState(
    val isRefreshing: Boolean = false,
    val refreshErrorMessage: String? = null
)

data class OnBoardingUiState(
    val shouldShowOnBoarding: Boolean = false,
    val followableUsers: List<FollowsUser> = listOf()
)


data class PostsFeedUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = listOf(), //empty list
    val loadingErrorMessage: String? = null,
    val endReached: Boolean = false
)



/* replace callback from fun HomeScreen
/from PostListItem
onPostClick: (Post) -> Unit,
onProfileClick: (Int) -> Unit, // it same -- onUserClick: (FollowsUser) -> Unit,
onLikeClick: (String) -> Unit,
onCommentClick: (String) -> Unit,
......
 */

sealed interface HomeUiAction {

    data class FollowUserAction(val user: FollowsUser) : HomeUiAction

    data class PostLikeAction(val post: Post): HomeUiAction

    data object RemoveOnboardingAction : HomeUiAction

    data object RefreshAction : HomeUiAction

    data object LoadMorePostsAction : HomeUiAction
}



/* Пример с fun followUser -> onBoardingUiState
val users = listOf(
    FollowsUser(id = 1, name = "Alice", bio = "Bio", isFollowing = false),
    FollowsUser(id = 2, name = "Bob", bio = "Bio", isFollowing = true)
)
Если вызывается followUser для пользователя с id = 1 (Alice), то логика map будет такой:

Для первого пользователя (Alice), id совпадает:
Создается копия Alice с isFollowing = true.
Для второго пользователя (Bob), id не совпадает:
Возвращается Bob без изменений.
Новый список будет выглядеть так:

kotlin
Копировать код
val updatedUsers = listOf(
    FollowsUser(id = 1, name = "Alice", bio = "Bio", isFollowing = true),
    FollowsUser(id = 2, name = "Bob", bio = "Bio", isFollowing = true)
)


 */












