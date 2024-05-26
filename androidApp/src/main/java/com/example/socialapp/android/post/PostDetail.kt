package com.example.socialapp.android.post

import androidx.compose.runtime.Composable
import com.example.socialapp.android.common.fake_data.Post
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun PostDetail(
    navigator: DestinationsNavigator,
    postId: String
) {

    val viewModel: PostDetailViewModel = koinViewModel()

    PostDetailScreen(
        postUiState = viewModel.postUiState,
        commentsUiState = viewModel.commentsUiState,
        onCommentMoreIconClick = {},
        onProfileClick = {},
        onAddCommentClick = { /*TODO*/ },
        fetchData = {viewModel.fetchData(postId = postId)}
    )

}