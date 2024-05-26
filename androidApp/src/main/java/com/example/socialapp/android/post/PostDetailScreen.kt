package com.example.socialapp.android.post

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.R
import com.example.socialapp.android.common.components.CommentListItem
import com.example.socialapp.android.common.components.PostListItem
import com.example.socialapp.android.common.fake_data.Comment
import com.example.socialapp.android.common.fake_data.sampleComments
import com.example.socialapp.android.common.fake_data.samplePosts
import com.example.socialapp.android.common.theme.MediumSpacing
import com.example.socialapp.android.common.theme.SocialAppTheme


@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,

    postUiState: PostUiState,
    commentsUiState: CommentsUiState,

    onCommentMoreIconClick: (Comment) -> Unit,
    onProfileClick: (Int) -> Unit,
    onAddCommentClick: () -> Unit,//button with "Add comment"

    fetchData: () -> Unit // retry request
) {

    if (postUiState.isLoading && commentsUiState.isLoading){
        //show progress indicator
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }else if (postUiState.post != null){
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
        ){
            item(key = "post_item"){
                PostListItem(
                    post = postUiState.post,
                    onPostClick ={},
                    onProfileClick = onProfileClick,
                    onLikeClick = { /*TODO*/ }, //later
                    onCommentClick = { /*TODO*/ },
                    isDetailScreen = true//show all description
                )
            }

            item(key = "comments_header_section"){
                CommentsSectionHeader {
                    onAddCommentClick()
                }
            }

            items(items = sampleComments, key = {comment -> comment.id}){
                Divider()
                CommentListItem(
                    comment = it,
                    onProfileClick = onProfileClick
                ) {
                    onCommentMoreIconClick(it) // *** button
                }
            }
        }
    }else{
        //show error message
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(LargeSpacing)
            ) {
                Text(
                    text = stringResource(id = R.string.loading_error_message),
                    style = MaterialTheme.typography.bodySmall
                )
                
                OutlinedButton(onClick = fetchData) {
                    Text(text = stringResource(id = R.string.retry_button_text))
                }
                
            }
        }
    }

    //fetch data
    LaunchedEffect(
        key1 = Unit, // call this ones, when we enter composition
        block = { fetchData()}
    )

}


@Composable
fun CommentsSectionHeader(
    modifier: Modifier = Modifier,
    onAddCommentClick:() -> Unit //Add Comment Button
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(LargeSpacing),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween  // text and button
    ) {
        Text(
            text = stringResource(id = R.string.comments_label),
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedButton(onClick = onAddCommentClick) {
            Text(text = stringResource(id = R.string.add_comment_button_label))

        }
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PostDetailScreenPreview() {
    SocialAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            PostDetailScreen(
                postUiState = PostUiState(
                    isLoading = false,
                    post = samplePosts.first()
                ),
                commentsUiState = CommentsUiState(
                    isLoading = false,
                    comments = sampleComments
                ),
                onCommentMoreIconClick = {},
                onProfileClick = {},
                onAddCommentClick = { /*TODO*/ },
                fetchData = {}
            )
        }
    }
}