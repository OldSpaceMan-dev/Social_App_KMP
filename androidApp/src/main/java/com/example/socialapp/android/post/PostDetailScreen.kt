package com.example.socialapp.android.post

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.R
import com.example.socialapp.android.common.components.CircleImage
import com.example.socialapp.android.common.components.CommentListItem
import com.example.socialapp.android.common.components.PostListItem
import com.example.socialapp.android.common.components.ScreenLevelLoadingErrorView
import com.example.socialapp.android.common.components.ScreenLevelLoadingView
import com.example.socialapp.android.common.components.loadingMoreItem

import com.example.socialapp.android.common.fake_data.sampleComments
import com.example.socialapp.android.common.fake_data.samplePosts
import com.example.socialapp.android.common.theme.ExtraLargeSpacing
import com.example.socialapp.android.common.theme.MediumSpacing
import com.example.socialapp.android.common.theme.SmallSpacing
import com.example.socialapp.android.common.theme.SocialAppTheme
import com.example.socialapp.android.common.util.Constants
import com.example.socialapp.android.common.util.toCurrentUrl
import com.example.socialapp.post.domain.model.PostComment
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,

    postUiState: PostUiState,
    commentsUiState: CommentsUiState,
    postId: Long,
    onProfileNavigation: (userId: Long) -> Unit,

    onUiAction: (PostDetailUiAction) -> Unit
) {

    //paginashion - show last post and reload new post
    //Создает и запоминает состояние списка
    val listState = rememberLazyListState()
    val shouldFetchMoreComments by remember {
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


    var commentText by rememberSaveable { mutableStateOf("") }
    //скрывать клавиатуру когда коммент отправляем
    val keyboardController = LocalSoftwareKeyboardController.current


    //для ботом шита

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false // Отключаем промежуточное состояние, если нужно
    )



    val scope = rememberCoroutineScope()
    var selectedComment by rememberSaveable(stateSaver = postCommentSaver) {
        mutableStateOf(null)
    }


    // отображение бом щита
    var isBottomSheetVisible by remember { mutableStateOf(false) }


    // Логирование текущего состояния листа
    // Жесткая блокировка автоматического перехода в состояние Expanded
    LaunchedEffect(sheetState.currentValue) {
        Log.d("SheetState", "Sheet state changed: ${sheetState.currentValue}")
        Log.d("SheetState", "Sheet state changed: ${isBottomSheetVisible}")
    }



    /*
    if (isBottomSheetVisible) {
    ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide() // Закрываем лист
                    isBottomSheetVisible = false // Устанавливаем флаг в false, когда лист закрывается
                    //selectedComment = null // Сбрасываем выбранный комментарий при закрытии
                }
            },
            content = {
                selectedComment?.let { postComment ->
                    CommentMoreActionsBottomSheetContent(
                        comment = postComment,
                        canDeleteComment = postComment.userId == postUiState.post?.userId,
                        onDeleteCommentClick = { comment ->
                            scope.launch {
                                sheetState.hide()
                                isBottomSheetVisible = false
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onUiAction(PostDetailUiAction.RemoveCommentAction(comment))
                                    selectedComment = null // сброс выбранного коммента
                                }
                            }
                        },
                        onNavigateToProfile = { userId ->
                            scope.launch {
                                sheetState.hide()
                                isBottomSheetVisible = false
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    selectedComment = null
                                    onProfileNavigation(userId)
                                }
                            }
                        }
                    )
                }

            },
            modifier = Modifier.navigationBarsPadding()
        )
        }
     */



    if (isBottomSheetVisible) {

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide() // Закрываем лист
                    isBottomSheetVisible = false // Устанавливаем флаг в false, когда лист закрывается
                    //selectedComment = null // Сбрасываем выбранный комментарий при закрытии
                }
            },
            modifier = modifier.navigationBarsPadding(),
            dragHandle = null, // Drag handle можно отключить, если не требуется
        ) {
            selectedComment?.let { postComment ->
                CommentMoreActionsBottomSheetContent(
                    comment = postComment,
                    canDeleteComment =
                        postComment.userId == postUiState.post?.userId || postComment.isOwner,
                    onDeleteCommentClick = { comment ->
                        scope.launch {
                            sheetState.hide()
                            isBottomSheetVisible = false
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                selectedComment = null // сброс выбранного коммента
                                onUiAction(PostDetailUiAction.RemoveCommentAction(comment))
                            }
                        }
                    },
                    onNavigateToProfile = { userId ->
                        scope.launch {
                            sheetState.hide()
                            isBottomSheetVisible = false
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                selectedComment = null
                                onProfileNavigation(userId)
                            }
                        }
                    }
                )
            }

        }
    }






    if (postUiState.isLoading) {

        //show progress indicator
        ScreenLevelLoadingView()
    } else if (postUiState.post != null) {

        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.surface
                    )
                    .weight(1f),
                state = listState
            ) {
                item(key = "post") {
                    PostListItem(
                        post = postUiState.post,
                        //onPostClick ={},
                        onProfileClick = {onProfileNavigation(postUiState.post.userId)},
                        onLikeClick = { onUiAction(PostDetailUiAction.LikeOrDislikePostAction(it)) },
                        onCommentClick = { /*TODO*/ },
                        isDetailScreen = true,//show all description,
                        onPostDotsClick = { /*TODO*/ }
                    )
                }

                item(key = "comments_header_section") {
                    CommentsSectionHeader(
                        //onAddCommentClick = {}
                    )
                }

                if (commentsUiState.isAddingNewComment) {
                    loadingMoreItem()
                }


                items(
                    items = commentsUiState.comments,
                    key = { comment -> comment.commentId }
                ) { postComment ->


                    // Логирование каждого элемента отдельно
                    commentsUiState.comments.forEach { comment ->
                        Log.d("CommentList", "CommentId: ${comment.commentId}")
                    }

                    Divider()
                    CommentListItem(
                        comment = postComment, //it
                        onProfileClick = {onProfileNavigation(postComment.userId)},
                        onMoreIconClick = {
                            selectedComment = it
                            isBottomSheetVisible = true // Показываем лист
                            scope.launch {
                                sheetState.show()
                            }
                        }
                    )
                }

                if (commentsUiState.isLoading) {
                    loadingMoreItem()
                }

            }


            CommentInput(
                commentText = commentText,
                onCommentChange = {
                    commentText = it
                }, //{ newText -> commentText = newText } - явная запись
                onSendClick = {
                    keyboardController?.hide()
                    //Log.d("addNewComment UI", "Commemt text = ${it}")
                    onUiAction(PostDetailUiAction.AddCommentAction(it))
                    commentText = ""
                }
            )


        }

    } else {
        //show error message
        ScreenLevelLoadingErrorView {
            onUiAction(PostDetailUiAction.FetchPostAction(postId))
        }
    }

    //fetch data
    LaunchedEffect(
        key1 = Unit, // call this ones, when we enter composition
        block = { onUiAction(PostDetailUiAction.FetchPostAction(postId)) }
    )

    LaunchedEffect(key1 = shouldFetchMoreComments) {
        if (shouldFetchMoreComments && !commentsUiState.endReached) {
            onUiAction(PostDetailUiAction.LoadMoreCommentsAction)
        }
    }


}


@Composable
fun CommentsSectionHeader(
    modifier: Modifier = Modifier,
    //onAddCommentClick:() -> Unit //Add Comment Button
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
    }
}


@Composable
private fun CommentInput (
    modifier: Modifier = Modifier,
    commentText: String,
    onCommentChange: (String) -> Unit,
    onSendClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .animateContentSize()
    ) {
        Divider()

        Row(
            modifier = modifier.padding(
                horizontal = LargeSpacing,
                vertical = MediumSpacing
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LargeSpacing)
        ) {
            Box(
                modifier = modifier
                    .heightIn(min = 35.dp, max = 70.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(percent = 25)
                    )
                    .padding(
                        horizontal = MediumSpacing,
                        vertical = SmallSpacing
                    )
                    .weight(1f)
            ) {
                BasicTextField(
                    value = commentText,//коммент который будет отправлять
                    onValueChange = onCommentChange,
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    textStyle = LocalTextStyle.current.copy(
                        color = LocalContentColor.current),
                    cursorBrush = SolidColor(LocalContentColor.current)
                )

                //заполняем когда поле пустое
                if (commentText.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = SmallSpacing),
                        text = stringResource(id = R.string.comment_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) // прозрачность теста в подсказке
                    )
                }
            }

            SendCommentButton(
                sendCommentEnabled = commentText.isNotBlank(),
                onSendClick = {onSendClick(commentText)}
            )

        }
    }
}


@Composable
private fun CommentMoreActionsBottomSheetContent (
    modifier: Modifier = Modifier,
    comment: PostComment,
    canDeleteComment: Boolean, //только юзер или own может удалить коммент
    onDeleteCommentClick: (comment: PostComment) -> Unit,
    onNavigateToProfile: (userId: Long) -> Unit
) {

    Column {
        Text(
            text = stringResource(id = R.string.comment_more_actions_title),
            style = MaterialTheme.typography.titleSmall,
            modifier = modifier.padding(all = LargeSpacing)
        )

        Divider()



        ListItem(
            modifier = modifier.clickable {
                onNavigateToProfile(comment.userId)
            },
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.view_profile_action, comment.userName),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // многоточие
                )
            },

            leadingContent = {
                CircleImage(
                    url = comment.userImageUrl?.toCurrentUrl(),
                    modifier = modifier
                        .size(25.dp),
                    onClick = {}
                )
            }
        )

        ListItem(

            modifier = modifier.clickable(
                enabled = canDeleteComment, //true - достно удаление
                onClick = {
                    onDeleteCommentClick(comment)
                })
                .graphicsLayer {
                    // Устанавливаем прозрачность 90%, если удаление недоступно
                    alpha = if (canDeleteComment) 1f else 0.4f
                }
            ,
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.delete_comment_action),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // многоточие
                )
            },

            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        )
        Spacer(modifier = Modifier.height(40.dp))

    }
}







@Composable
private fun SendCommentButton(
    modifier: Modifier = Modifier,
    sendCommentEnabled: Boolean, //когда кнопка октивна или нет
    onSendClick: () -> Unit
) {
    val border = if (!sendCommentEnabled) {
        BorderStroke( // обводка строки
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    } else {
        null
    }

    Button(
        modifier = modifier.height(35.dp),
        enabled = sendCommentEnabled,
        onClick = onSendClick,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = Color.Transparent,//прозрачный
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        ),
        border = border,
        shape = RoundedCornerShape(percent = 50),
        elevation = ButtonDefaults.elevatedButtonElevation(//кнопка плоская -без теней
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = stringResource(id = R.string.send_button_text),
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
}





//можем сохранять данные избегая изменения в конфигурации
//можно реализовать и через вьюмодел
private val postCommentSaver = Saver<PostComment?, Any>(
    save = { postComment ->
        if (postComment != null) {
            mapOf(
                "commentId" to postComment.commentId,
                "content" to postComment.content,
                "postId" to postComment.postId,
                "userId" to postComment.userId,
                "userName" to postComment.userName,
                "userImageUrl" to postComment.userImageUrl,
                "createdAt" to postComment.createdAt
            )
        } else {
            null
        }
    },
    restore = { savedValue ->
        val map = savedValue as Map<*, *>
        PostComment(
            commentId = map["commentId"] as Long,
            content = map["content"] as String,
            postId = map["postId"] as Long,
            userId = map["userId"] as Long,
            userName = map["userName"] as String,
            userImageUrl = map["userImageUrl"] as String?,
            createdAt = map["createdAt"] as String
        )
    }
)







@Preview
@Composable
private fun CommentInputPreview() {
    CommentInput(
        commentText = "",
        onCommentChange = {},
        onSendClick = {}
    )
    
}

@Preview
@Composable
private fun SendCommentButtonPreview() {
    SendCommentButton(sendCommentEnabled = true, onSendClick = {})
}


@Preview
@Composable
private fun CommentMoreActionsBottomSheetContentPreview() {
    CommentMoreActionsBottomSheetContent(
        comment = sampleComments.first().toDomainComment(),
        canDeleteComment = false,
        onDeleteCommentClick = {},
        onNavigateToProfile = {}
    )
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
                    post = samplePosts.first().toDomainPost()
                ),
                commentsUiState = CommentsUiState(
                    isLoading = false,
                    comments = sampleComments.map { it.toDomainComment() }
                ),
                postId = 1,
                onProfileNavigation = {},
                onUiAction = {}
            )
        }
    }
}