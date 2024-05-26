package com.example.socialapp.android.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.socialapp.android.common.components.PostListItem
import com.example.socialapp.android.common.fake_data.FollowsUser
import com.example.socialapp.android.common.fake_data.Post
import com.example.socialapp.android.common.fake_data.samplePosts
import com.example.socialapp.android.common.fake_data.sampleUsers
import com.example.socialapp.android.common.theme.SocialAppTheme
import com.example.socialapp.android.home.onboarding.OnBoardingSection
import com.example.socialapp.android.home.onboarding.OnBoardingUiState
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
// UiState
    onBoardingUiState: OnBoardingUiState,
    postsUiState: PostsUiState,
//from PostListItem
    onPostClick: (Post) -> Unit,  
    onProfileClick: (Int) -> Unit, // it same -- onUserClick: (FollowsUser) -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
//from onBoarding
    
    onFollowButtonClick: (Boolean, FollowsUser) -> Unit,
    onBoardingFinish: () -> Unit,

    fetchData: () -> Unit // извлечь данные
) {
    //рефреш страницы
    val pullRefreshState = rememberPullRefreshState(
        refreshing = onBoardingUiState.isLoading && postsUiState.isLoading,
        onRefresh = { fetchData() }
    )


    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ){
        
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ){
            if (onBoardingUiState.shouldShowOnBoarding){
                item(key = "onboardingsection") {
                    OnBoardingSection(
                        users = onBoardingUiState.users,
                        onUserClick = {onProfileClick(it.id)},
                        onFollowButtonClick = onFollowButtonClick
                    ) {
                        onBoardingFinish()
                    }
                }
            }

            items(
                items = postsUiState.posts,
                key = {post -> post.id}
            ){
                PostListItem(
                    post = it,
                    onPostClick = onPostClick,
                    onProfileClick = onProfileClick,
                    onLikeClick = onLikeClick,
                    onCommentClick =  onCommentClick
                )

            }


        }

        PullRefreshIndicator(
            refreshing = onBoardingUiState.isLoading && postsUiState.isLoading,
            state = pullRefreshState,
            modifier = modifier.align(Alignment.TopCenter)
        )

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
                onBoardingUiState = OnBoardingUiState(
                    users = sampleUsers,
                    shouldShowOnBoarding = true
                ),
                postsUiState = PostsUiState(
                    posts = samplePosts
                ),
                onPostClick = {},
                onProfileClick = {},
                onLikeClick = {  },
                onCommentClick = {  },
                onFollowButtonClick = { _, _ -> },
                onBoardingFinish = {},
                fetchData = {}
            )
        }
    }
    
}








