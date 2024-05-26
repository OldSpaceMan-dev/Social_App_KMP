package com.example.socialapp.android.account.profile

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialapp.android.R
import com.example.socialapp.android.common.components.CircleImage
import com.example.socialapp.android.common.components.FollowButton
import com.example.socialapp.android.common.components.PostListItem
import com.example.socialapp.android.common.fake_data.Post
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.common.theme.MediumSpacing
import com.example.socialapp.android.common.theme.SmallSpacing
import com.example.socialapp.android.common.theme.SocialAppTheme


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userInfoUiState: UserInfoUiState,
    profilePostsUiState: ProfilePostsUiState,

    onButtonClick: () -> Unit, // from HeaderSection
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,

    onPostClick: (Post) -> Unit,
    onLikeClick: (String) -> Unit, // String it will be post id
    onCommentClick: (String) -> Unit, // String it will be post id

    fetchData: () -> Unit // ProfileViewModel - start fetching user profile
) {

    //circle indicator
    if (userInfoUiState.isLoading && profilePostsUiState.isLoading){
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }else{
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ){
            item(key = "header_section"){
                ProfileHeaderSection(
                    imageUrl = userInfoUiState.profile?.profileUrl ?: "",
                    name = userInfoUiState.profile?.name ?: "",
                    bio = userInfoUiState.profile?.bio ?: "",
                    followersCount = userInfoUiState.profile?.followersCount ?: 0,
                    followingCount = userInfoUiState.profile?.followingCount ?: 0,
                    onButtonClick = onButtonClick,
                    onFollowersClick = onFollowersClick,
                    onFollowingClick = onFollowingClick
                )
            }

            items(
                items = profilePostsUiState.posts,
                key = {post -> post.id}
            ){
                PostListItem(
                    post = it,
                    onPostClick = onPostClick,
                    onProfileClick = {},
                    onLikeClick = onLikeClick,
                    onCommentClick = onCommentClick
                )
            }
        }
    }

    //we need fetchData call back
    LaunchedEffect(key1 = Unit){
        fetchData()
    }


}





@Composable
fun ProfileHeaderSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
    name: String,
    bio: String,
    followersCount: Int,
    followingCount: Int,

    isCurrentUser: Boolean = false,
    isFollowing: Boolean = false, // if not the currUser

    onButtonClick: () -> Unit, // click on "Follow or Edit"
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = MediumSpacing)
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(all = LargeSpacing) // тут будет сумма padding
    ) {

        CircleImage(
            modifier = modifier.size(90.dp),
            imageUrl = imageUrl,
            onClick = {}
        ) 
        
        Spacer(modifier = modifier.height(SmallSpacing))
        
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis 
        )
        
        Text(
            text = bio,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = modifier.height(MediumSpacing))

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = modifier.weight(1f) // take remaining space after the follow button
                
            ) {

                FollowsText(
                    count = followersCount,
                    text = R.string.followers_text,
                    onClick = onFollowersClick
                )
                
                Spacer(modifier = modifier.width(MediumSpacing))

                FollowsText(
                    count = followingCount,
                    text = R.string.following_text,
                    onClick = onFollowingClick
                )
            }

            FollowButton(
                text = R.string.follow_text_label, // change later - for outline button
                onClick = onButtonClick,
                modifier = modifier
                    .height(30.dp)
                    .widthIn(10.dp),
                // if isCurrentUser = Edit
                isOutlined = isCurrentUser || isFollowing
            )

        }
        
    }
}


@Composable
fun FollowsText(
    modifier: Modifier = Modifier,
    count: Int,
    @StringRes text: Int,
    onClick: () -> Unit
) {

    Text(
        text = buildAnnotatedString {
            //text stile for Follower/ing text and coutn
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            ){
                append(text = "$count ")
            }
            
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            ){
                append(text = stringResource(id = text))
            }
        },

        modifier = modifier.clickable { onClick() }
    )
}





@Preview
@Composable
fun FollowsTextPreview() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            FollowsText(
                count = 2,
                text = R.string.follow_text_label,
                onClick = {}
            )
        }
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileHeaderSectionPreview() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            ProfileHeaderSection(
                imageUrl = "",
                name = "Arkadiy",
                bio = "Hey there, welcome to Arkadiy page",
                followersCount = 100,
                followingCount = 3,
                onButtonClick = { /*TODO*/ },
                onFollowersClick = { /*TODO*/ },
                ) {
                
            }
        }
    }
    
}










