package com.example.socialapp.android.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.socialapp.android.common.components.PostListItem
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.common.theme.MediumSpacing

import com.example.socialapp.android.common.theme.SocialAppTheme
import com.example.socialapp.android.common.util.Constants
import com.example.socialapp.android.home.onboarding.OnBoardingSection
import com.example.socialapp.common.domain.model.Post
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
// UiState
    onBoardingUiState: OnBoardingUiState,
    postsFeedUiState: PostsFeedUiState,
    homeRefreshState: HomeRefreshState,
//from PostListItem
    onUiAction: (HomeUiAction) -> Unit,
    onProfileNavigation: (userId: Long) -> Unit ,
    onPostDetailNavigation: (Post) -> Unit,

) {
    //рефреш страницы
    val pullRefreshState = rememberPullRefreshState(
        refreshing = homeRefreshState.isRefreshing, //onBoardingUiState.isLoading && postsFeedUiState.isLoading,
        onRefresh = { onUiAction(HomeUiAction.RefreshAction) }
    )


    //paginashion - show last post and reload new post
    //Создает и запоминает состояние списка
    val listState = rememberLazyListState()
    val shouldFetchMorePosts by remember {
        //derived-наследуемый (полученный)
        derivedStateOf {
            //текущего состояния списка - количество элем/ информацию о видимых элем.
            val layoutInfo = listState.layoutInfo
            //информации о видимых элементах
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            //если нет ни одного элемента
            if (layoutInfo.totalItemsCount == 0) {
                false
            }else {
                // Получает последний видимый элемент.
                val lastVisibleItem = visibleItemsInfo.last()
                //+1 тк индексы в списке с 0, а кол-во эл с 1
                //те индекс 9 (last) +1 а элем=10
                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount)
            }

        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ){
        
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState
        ){
            if (onBoardingUiState.shouldShowOnBoarding){
                item {
                    OnBoardingSection(
                        users = onBoardingUiState.followableUsers,
                        onUserClick = {onProfileNavigation(it.id)},
                        onFollowButtonClick = { _, user ->
                            onUiAction(
                                HomeUiAction.FollowUserAction(user)
                            )
                        },
                        onBoardingFinish = { onUiAction(HomeUiAction.RemoveOnboardingAction) }
                    )

                    // place text
                }
            }

            items(
                items = postsFeedUiState.posts,
                key = {post -> post.postId}
            ){post ->
                PostListItem(
                    post = post,
                    onPostClick = { onPostDetailNavigation(it) },
                    onProfileClick = { onProfileNavigation(it)},
                    onLikeClick = { onUiAction(HomeUiAction.PostLikeAction(it))},
                    onCommentClick =  { onPostDetailNavigation(it) }
                )
            }

            if (postsFeedUiState.isLoading && postsFeedUiState.posts.isNotEmpty()) {
                item(key = Constants.LOADING_MORE_ITEM_KEY) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(vertical = MediumSpacing, horizontal = LargeSpacing),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

        }

        PullRefreshIndicator(
            refreshing = homeRefreshState.isRefreshing,//onBoardingUiState.isLoading && postsFeedUiState.isLoading,
            state = pullRefreshState,
            modifier = modifier.align(Alignment.TopCenter)
        )
    }

    LaunchedEffect(key1 = shouldFetchMorePosts) {
        if (shouldFetchMorePosts && !postsFeedUiState.endReached) {
            onUiAction(HomeUiAction.LoadMorePostsAction)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
fun HomeScreenPreview() {
    SocialAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(
                onBoardingUiState = OnBoardingUiState(),
                postsFeedUiState = PostsFeedUiState(),
                homeRefreshState = HomeRefreshState(),
                onPostDetailNavigation = {},
                onProfileNavigation = {},
                onUiAction = {}
            )
        }
    }
    
}








