package com.example.socialapp.android.common.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.socialapp.android.R
import com.example.socialapp.android.common.fake_data.SamplePost
import com.example.socialapp.android.common.fake_data.samplePosts
import com.example.socialapp.android.common.theme.ExtraLargeSpacing
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.common.theme.MediumSpacing
import com.example.socialapp.android.common.theme.SocialAppTheme
import com.example.socialapp.android.common.util.toCurrentUrl
import com.example.socialapp.common.domain.model.Post

@Composable
fun PostListItem(
    modifier: Modifier = Modifier,
    post: Post,
    onPostClick: (Post) -> Unit,
    onProfileClick: (userId: Long) -> Unit,
    onLikeClick: (Post) -> Unit,
    onCommentClick: (Post) -> Unit,
    isDetailScreen: Boolean = false

) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            //.aspectRatio(ratio = 0.7f) // 7:10 || width : height
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable { onPostClick(post) }
            .padding(bottom = ExtraLargeSpacing)
    ) {

        PostHeader(
            name = post.userName,
            profileUrl = post.userImageUrl,
            date = post.createdAt,
            onProfileClick = {
                onProfileClick(post.userId)
            }
        )

        AsyncImage(
            model = post.imageUrl.toCurrentUrl(),
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1.0f),
            contentScale = ContentScale.Crop,

            placeholder = if (isSystemInDarkTheme()){
                painterResource(id = R.drawable.dark_image_place_holder)
            }else{
                painterResource(id = R.drawable.light_image_place_holder)
            },
        )

        PostLikesRow(
            likesCount = post.likesCount,
            commentsCount = post.commentsCount,
            onLikeClick = {onLikeClick(post)},
            isPostLiked = post.isLiked,
            onCommentClick = {onCommentClick(post)}
        )

        Text(
            text = post.caption,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
                .padding(horizontal = LargeSpacing),
            maxLines = if (isDetailScreen){
                20
            }else{
                2
            },
            //как следует обрабатывать визуальное переполнение.
            overflow = TextOverflow.Ellipsis // многточие
        )
    }
}



@Composable
fun PostHeader(
    modifier: Modifier = Modifier,
    name: String,
    profileUrl: String?,
    date: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = LargeSpacing,
                vertical = LargeSpacing
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
    ) {

        CircleImage(
            url = profileUrl?.toCurrentUrl(),
            modifier = modifier.size(30.dp),
            onClick = onProfileClick
        )

        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Box(modifier = modifier
            .size(4.dp)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = modifier.weight(1f) // оставшееся пространство поле горизонтального меню ***
        )

        Icon(
            painter = painterResource(id = R.drawable.round_more_horizontal),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant //оттенок, который будет применен к painter. Если указан цвет.
        )

    }

}


@Composable
fun PostLikesRow(
    modifier: Modifier = Modifier,
    likesCount: Int,
    commentsCount: Int,
    onLikeClick: () -> Unit,
    isPostLiked: Boolean,
    onCommentClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 0.dp,
                horizontal = MediumSpacing
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onLikeClick) {
            Icon(
                painter = if (isPostLiked) {
                    painterResource(id = R.drawable.like_icon_filled)
                } else {
                    painterResource(id = R.drawable.like_icon_outlined)
                },
                contentDescription = null,

                // добавить условие на isPostLiked
                tint = if (isPostLiked) {
                    Color.Red // Используем ярко-красный цвет для лайкнутого состояния
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant // Используем стандартный цвет для не лайкнутых постов
                }
            )
        }

        Text(
            text = "$likesCount",
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 18.sp
            )
        )

        Spacer(modifier = modifier.width(MediumSpacing))



        IconButton(onClick = onCommentClick) {
            Icon(
                painter = painterResource(id = R.drawable.chat_icon_outlined),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }

        Text(
            text = "$commentsCount",
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 18.sp
            )
        )
    }
}



@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
private fun PostListItemPreview() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            PostListItem(
                post = samplePosts.first().toDomainPost(),
                onPostClick = {},
                onProfileClick = {},
                onCommentClick = {},
                onLikeClick = {}
            )
        }
    }
}





@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview()
@Composable
private fun PostHeaderPreview() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            PostHeader(
                name = "Mr Smith",
                profileUrl = "",
                date = "20 min",
                onProfileClick = {}
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
private fun PostLikesRowPreview() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            PostLikesRow(
                likesCount = 12,
                commentsCount = 2,
                onLikeClick = {},
                isPostLiked = true,
                onCommentClick = {}
            )
        }
    }
}