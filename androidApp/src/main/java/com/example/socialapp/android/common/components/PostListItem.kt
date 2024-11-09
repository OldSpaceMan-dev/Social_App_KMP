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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.socialapp.android.R
import com.example.socialapp.android.common.fake_data.samplePosts
import com.example.socialapp.android.common.theme.ExtraLargeSpacing
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.common.theme.MediumSpacing
import com.example.socialapp.android.common.theme.SocialAppTheme
import com.example.socialapp.android.common.util.toCurrentUrl
import com.example.socialapp.common.domain.model.Post
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListItem(
    modifier: Modifier = Modifier,
    post: Post,
    onPostClick: ((Post) -> Unit)? = null, //делаем карточку не кликабельной - обнуляем функцию
    onProfileClick: (userId: Long) -> Unit,
    onLikeClick: (Post) -> Unit,
    onCommentClick: (Post) -> Unit,
    isDetailScreen: Boolean = false,

    onPostMoreIconClick: (Post) -> Unit,
) {






    Column(
        modifier = modifier
            .fillMaxWidth()
            //.aspectRatio(ratio = 0.7f) // 7:10 || width : height
            .background(color = MaterialTheme.colorScheme.surface)

            //.clickable { onPostClick(post) }
            .let { mod ->
                if (onPostClick != null) {
                    mod
                        .clickable { onPostClick(post) }
                        .padding(bottom = ExtraLargeSpacing)
                } else {
                    mod
                }
            }
    ) {

        PostHeader(
            name = post.userName,
            profileUrl = post.userImageUrl,
            date = post.createdAt,
            onProfileClick = {
                onProfileClick(post.userId)
            },
            onPostMoreIconClick = {onPostMoreIconClick(post)}
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
    onProfileClick: () -> Unit,
    onPostMoreIconClick: () -> Unit
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



        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onProfileClick)
        ) {
            CircleImage(
                url = profileUrl?.toCurrentUrl(),
                modifier = modifier.size(30.dp),
                onClick = onProfileClick
            )

            Spacer(modifier = Modifier.width(MediumSpacing))

            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable(onClick = onProfileClick)
            )
        }


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
            tint = MaterialTheme.colorScheme.onSurfaceVariant, //оттенок, который будет применен к painter. Если указан цвет.
            modifier = modifier.clickable { onPostMoreIconClick()}
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







//можем сохранять данные избегая изменения в конфигурации
//можно реализовать и через вьюмодел
private val postActionSaver = Saver<Post?, Any>(
    save = { post ->
        if (post != null) {
            mapOf(

                "postId" to post.postId,
                "caption" to post.caption,
                "imageUrl" to post.imageUrl,
                "createdAt" to post.createdAt,
                "likesCount" to post.likesCount,
                "commentsCount" to post.commentsCount,
                "userId" to post.userId,
                "userName" to post.userName,
                "userImageUrl" to post.userImageUrl,
                "isLiked" to post.isLiked
            )
        } else {
            null
        }
    },
    restore = { savedValue ->
        val map = savedValue as Map<*, *>
        Post(
            postId = map["postId"] as Long,
            caption = map["caption"] as String,
            imageUrl = map["imageUrl"] as String,
            createdAt = map["createdAt"] as String,
            likesCount = map["likesCount"] as Int,
            commentsCount = map["commentsCount"] as Int,
            userId = map["userId"] as Long,
            userName = map["userName"] as String,
            userImageUrl = map["userImageUrl"] as String?,
            isLiked = map["isLiked"] as Boolean, //TODO сделать по дефолту - надо смотреть
        )
    }
)








@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
private fun PostListItemPreview() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            PostListItem(
                post = samplePosts.first().toDomainPost(),
                //onPostClick = { },
                onProfileClick = {},
                onCommentClick = {},
                onLikeClick = {},
                onPostMoreIconClick = {}
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
                onProfileClick = {},
                onPostMoreIconClick = {}
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