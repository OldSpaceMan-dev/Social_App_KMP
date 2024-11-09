package com.example.socialapp.android.post

import androidx.compose.runtime.Composable
import com.example.socialapp.android.destinations.ProfileDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun PostDetail(
    navigator: DestinationsNavigator,
    postId: Long
) {

    val viewModel: PostDetailViewModel = koinViewModel()

    PostDetailScreen(
        postUiState = viewModel.postUiState,
        commentsUiState = viewModel.commentsUiState,
        //onCommentMoreIconClick = {},
        //onProfileClick = {},
        //onAddCommentClick = { /*TODO*/ },
        postId = postId,
        onProfileNavigation = { navigator.navigate(ProfileDestination(it))},
        onUiAction = viewModel::onUiAction,
        onPostDeleted = {navigator.popBackStack()}
    )

}

//navigator.navigate(ProfileDestination(it)