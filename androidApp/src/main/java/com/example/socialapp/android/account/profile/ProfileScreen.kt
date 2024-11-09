package com.example.socialapp.android.account.profile

import android.content.res.Configuration
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Animation
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.socialapp.android.R
import com.example.socialapp.android.common.components.CircleImage
import com.example.socialapp.android.common.components.FollowButton
import com.example.socialapp.android.common.components.PostListItem
import com.example.socialapp.android.common.fake_data.samplePosts
import com.example.socialapp.android.common.fake_data.sampleProfiles
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.common.theme.MediumSpacing
import com.example.socialapp.android.common.theme.SmallSpacing
import com.example.socialapp.android.common.theme.SocialAppTheme
import com.example.socialapp.android.common.util.Constants
import com.example.socialapp.android.common.util.toCurrentUrl
import com.example.socialapp.common.domain.model.Post


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userInfoUiState: UserInfoUiState,
    profilePostsUiState: ProfilePostsUiState,

    profileId: Long, //вводим композицию на экране профиля
    onUiAction: (ProfileUiAction) -> Unit,

    onFollowButtonClick: () -> Unit, // from HeaderSection
    onFollowersScreenNavigation: () -> Unit,
    onFollowingScreenNavigation: () -> Unit,
    onPostDetailNavigation: (Post) -> Unit

) {
    //Создает и запоминает состояние списка
    val listState = rememberLazyListState()
    val listGridState = rememberLazyGridState()


    //TODO для строчной пагинации
    //paginashion - show last post and reload new post
    val shouldFetchMorePosts by remember {
        //derived-наследуемый (полученный)
        derivedStateOf {
            //текущего состояния списка - количество элем/ информацию о видимых элем.
            val layoutInfo = listState.layoutInfo
            //информации о видимых элементах
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            //если нет ни одного элемента
            if (layoutInfo.totalItemsCount == 0) {
                Log.d("ProfileScreen", "No items in list, shouldFetchMorePostsList: false")
                false
            }else {
                // Получает последний видимый элемент.
                //val lastVisibleItem = visibleItemsInfo.last()

                // Получает последний видимый элемент.
                val lastVisibleItem = visibleItemsInfo.lastOrNull()
                val shouldFetch = lastVisibleItem != null && (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount)

                Log.d("ProfileScreen", "lastVisibleItem: $lastVisibleItem, shouldFetchMorePostsList: $shouldFetch")
                shouldFetch

                //+1 тк индексы в списке с 0, а кол-во эл с 1
                //те индекс 9 (last) +1 а элем=10
                //(lastVisibleItem.index + 1 == layoutInfo.totalItemsCount)
            }

        }
    }


    //val headerVisisble = remember { mutableStateOf(true) }

    val displayType = remember { mutableStateOf(DisplayType.LIST) }

    //val headerVisibilityAnimation = remember { Animatable(1f) }






    //circle indicator
    if (userInfoUiState.isLoading){
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }else{
        Column {

            if (displayType.value == DisplayType.LIST) {

                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    state = listState
                ){

                    item(key = "header_section"){
                        ProfileHeaderSection(
                            imageUrl = userInfoUiState.profile?.imageUrl ?: "",
                            name = userInfoUiState.profile?.name ?: "",
                            bio = userInfoUiState.profile?.bio ?: "",
                            followersCount = userInfoUiState.profile?.followersCount ?: 0,
                            followingCount = userInfoUiState.profile?.followingCount ?: 0,
                            postCount = 1,

                            isFollowing = userInfoUiState.profile?.isFollowing ?: false,
                            isCurrentUser = userInfoUiState.profile?.isOwnProfile ?:false,

                            onButtonClick = onFollowButtonClick,
                            onFollowersClick = onFollowersScreenNavigation,
                            onFollowingClick = onFollowingScreenNavigation,

                            )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconButton(
                                onClick = { displayType.value = DisplayType.LIST }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.list_posts_icon),
                                    contentDescription = null
                                )
                            }

                            IconButton(
                                onClick = {
                                    displayType.value = DisplayType.GRID
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id =  R.drawable.grid_posts_icon),
                                    contentDescription = null
                                )
                            }

                        }
                    }

                    items(
                        items = profilePostsUiState.posts,
                        key = {post -> post.postId}
                    ){
                        PostListItem(
                            post = it,
                            onPostClick = {},
                            onProfileClick = {},
                            onLikeClick = {post ->
                                onUiAction(ProfileUiAction.PostLikeAction(post))
                            },
                            onCommentClick = {},
                            onPostMoreIconClick = {}
                        )
                    }

                    //loading spinner
                    if (profilePostsUiState.isLoading){
                        item(key = Constants.LOADING_MORE_ITEM_KEY) {
                            Box(
                                modifier = modifier
                                    .fillMaxSize()
                                    .padding(
                                        vertical = MediumSpacing,
                                        horizontal = LargeSpacing
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                CircularProgressIndicator()
                            }
                        }
                    }

                }

            } else {

                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    state = listState
                ) {

                    item(key = "header_section"){
                        ProfileHeaderSection(
                            imageUrl = userInfoUiState.profile?.imageUrl ?: "",
                            name = userInfoUiState.profile?.name ?: "",
                            bio = userInfoUiState.profile?.bio ?: "",
                            followersCount = userInfoUiState.profile?.followersCount ?: 0,
                            followingCount = userInfoUiState.profile?.followingCount ?: 0,
                            postCount = 1,

                            isFollowing = userInfoUiState.profile?.isFollowing ?: false,
                            isCurrentUser = userInfoUiState.profile?.isOwnProfile ?:false,

                            onButtonClick = onFollowButtonClick,
                            onFollowersClick = onFollowersScreenNavigation,
                            onFollowingClick = onFollowingScreenNavigation,

                            )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconButton(
                                onClick = { displayType.value = DisplayType.LIST }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.list_posts_icon),
                                    contentDescription = null
                                )
                            }

                            IconButton(
                                onClick = {
                                    displayType.value = DisplayType.GRID
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id =  R.drawable.grid_posts_icon),
                                    contentDescription = null
                                )
                            }

                        }
                    }

                    // Встраивание LazyVerticalGrid внутри Box для ограничения его высоты
                    item {
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(600.dp) // задайте необходимую высоту
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                modifier = Modifier.fillMaxSize(),
                                state = listGridState
                            ) {
                                items(profilePostsUiState.posts) { post ->
                                    Box(
                                        modifier = Modifier
                                            .padding(0.5.dp) // Отступы между изображениями
                                            .fillMaxWidth()
                                            .aspectRatio(1.0f)
                                            .clickable { onPostDetailNavigation(post) }
                                        //.border(0.2.dp, Color.Gray) // Рамка вокруг изображения
                                    ) {
                                        AsyncImage(
                                            model = post.imageUrl.toCurrentUrl(),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    // Элемент загрузки
                    if (profilePostsUiState.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        vertical = MediumSpacing,
                                        horizontal = LargeSpacing
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
/*

        // Блок переключения отображения постов
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // Кнопка для отображения в виде списка
            TextButton(
                onClick = { displayType.value = DisplayType.LIST },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.comment_hint),
                    color = if (displayType.value == DisplayType.LIST) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }

            // Кнопка для отображения в виде сетки
            TextButton(
                onClick = { displayType.value = DisplayType.GRID },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.comment_hint),
                    color = if (displayType.value == DisplayType.GRID) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }


        if (displayType.value == DisplayType.LIST) {

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                state = listState
            ){
                item(key = "header_section"){
                    ProfileHeaderSection(
                        imageUrl = userInfoUiState.profile?.imageUrl ?: "",
                        name = userInfoUiState.profile?.name ?: "",
                        bio = userInfoUiState.profile?.bio ?: "",
                        followersCount = userInfoUiState.profile?.followersCount ?: 0,
                        followingCount = userInfoUiState.profile?.followingCount ?: 0,

                        isFollowing = userInfoUiState.profile?.isFollowing ?: false,
                        isCurrentUser = userInfoUiState.profile?.isOwnProfile ?:false,

                        onButtonClick = onFollowButtonClick,
                        onFollowersClick = onFollowersScreenNavigation,
                        onFollowingClick = onFollowingScreenNavigation,

                        displayType = displayType.value,
                        onDisplayTypeChange = {displayType.value = it}
                    )
                }

                items(
                    items = profilePostsUiState.posts,
                    key = {post -> post.postId}
                ){
                    PostListItem(
                        post = it,
                        onPostClick = {},
                        onProfileClick = {},
                        onLikeClick = {post ->
                            onUiAction(ProfileUiAction.PostLikeAction(post))
                        },
                        onCommentClick = {},
                        onPostMoreIconClick = {}
                    )
                }

                //loading spinner
                if (profilePostsUiState.isLoading){
                    item(key = Constants.LOADING_MORE_ITEM_KEY) {
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(
                                    vertical = MediumSpacing,
                                    horizontal = LargeSpacing
                                ),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator()
                        }
                    }
                }

            }

        } else {

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                state = listState
            ) {
                // Отдельный элемент для ProfileHeaderSection
                item {
                    ProfileHeaderSection(
                        imageUrl = userInfoUiState.profile?.imageUrl ?: "",
                        name = userInfoUiState.profile?.name ?: "",
                        bio = userInfoUiState.profile?.bio ?: "",
                        followersCount = userInfoUiState.profile?.followersCount ?: 0,
                        followingCount = userInfoUiState.profile?.followingCount ?: 0,

                        isFollowing = userInfoUiState.profile?.isFollowing ?: false,
                        isCurrentUser = userInfoUiState.profile?.isOwnProfile ?: false,

                        onButtonClick = onFollowButtonClick,
                        onFollowersClick = onFollowersScreenNavigation,
                        onFollowingClick = onFollowingScreenNavigation,

                        displayType = displayType.value,
                        onDisplayTypeChange = { displayType.value = it }
                    )
                }

                // Встраивание LazyVerticalGrid внутри Box для ограничения его высоты
                item {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(600.dp) // задайте необходимую высоту
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize(),
                            state = listGridState
                        ) {
                            items(profilePostsUiState.posts) { post ->
                                Box(
                                    modifier = Modifier
                                        .padding(0.5.dp) // Отступы между изображениями
                                        .fillMaxWidth()
                                        .aspectRatio(1.0f)
                                        .clickable { onPostDetailNavigation(post) }
                                        //.border(0.2.dp, Color.Gray) // Рамка вокруг изображения
                                ) {
                                    AsyncImage(
                                        model = post.imageUrl.toCurrentUrl(),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop,
                                    )
                                }
                            }
                        }
                    }
                }
                // Элемент загрузки
                if (profilePostsUiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    vertical = MediumSpacing,
                                    horizontal = LargeSpacing
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }


            }



        }



    }


*/

    //we need fetchData call back
    LaunchedEffect(key1 = Unit){
        onUiAction(ProfileUiAction.FetchProfileAction(profileId = profileId))
    }


    //send request to load more post
    LaunchedEffect(key1 = shouldFetchMorePosts) {
        if (shouldFetchMorePosts && !profilePostsUiState.endReached){
            onUiAction(ProfileUiAction.LoadMorePostsAction)
        }
    }


    //TODO для сетки пагинации
    // Логика пагинации для сетки LazyVerticalGrid (listGridState)
    LaunchedEffect(listGridState) {
        snapshotFlow { //наблюдение за изменениями внутри LazyGridState
            //получаем список видимых элементов - если нет элем то lastOrNull - null или ничего
            listGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collect { lastVisibleIndex ->
            //когда прокручивает LazyVerticalGrid и последний видимый элемент изменяется,
            // срабатывает новый вызов collect с обновленным lastVisibleIndex.
            val totalItems = listGridState.layoutInfo.totalItemsCount
            if (lastVisibleIndex == totalItems - 1 && !profilePostsUiState.endReached) {

                Log.d("ProfileScreen", "Автоматически подгружаем больше постов для GRID")
                //проверяем является ли элемент последним видимым - да - колл LoadMorePostsAction
                onUiAction(ProfileUiAction.LoadMorePostsAction)
            }
        }
    }


}

//отображение постов
enum class DisplayType { LIST, GRID }



@Composable
fun ProfileHeaderSection(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    name: String,
    bio: String,

    followersCount: Int,
    followingCount: Int,
    postCount: Int, //TODO нужно добавить колличество -постов

    isCurrentUser: Boolean = false,
    isFollowing: Boolean = false, // if not the currUser

    onButtonClick: () -> Unit, // click on "Follow or Edit"
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,


) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = MediumSpacing)
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(
                top= LargeSpacing,
                start = LargeSpacing,
                end = LargeSpacing) // тут будет сумма padding
    ) {


        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CircleImage(
                modifier = modifier.size(90.dp),
                url = imageUrl?.toCurrentUrl(),
                onClick = {}
            )

            Row (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                FollowsText(
                    count = postCount,
                    text = R.string.posts_count,
                    onClick = {}
                )

                FollowsText(
                    count = followersCount,
                    text = R.string.followers_text,
                    onClick = onFollowersClick
                )

                FollowsText(
                    count = followingCount,
                    text = R.string.following_text,
                    onClick = onFollowingClick
                )
            }
        }

        
        Spacer(modifier = modifier.height(SmallSpacing))



        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

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
            }


            FollowButton(
                text = when {//R.string.follow_text_label, // change later - for outline button
                    isCurrentUser -> R.string.edit_profile_label
                    isFollowing -> R.string.unfollow_text_label
                    else -> R.string.follow_text_label
                },
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
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$count",
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )

        Text(
            text = stringResource(id = text),
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )
    }
}



/*
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
 */


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileScreenPreview() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            ProfileScreen(
                userInfoUiState = UserInfoUiState(
                    isLoading = false,
                    profile = sampleProfiles.map { it.toDomainProfiel() }.first()
                ),
                profilePostsUiState = ProfilePostsUiState(
                    posts = samplePosts.map { it.toDomainPost() }
                ),
                profileId = 123,
                onUiAction = {},
                onFollowButtonClick = { /*TODO*/ },
                onFollowersScreenNavigation = { /*TODO*/ },
                onFollowingScreenNavigation = { /*TODO*/ },
                onPostDetailNavigation = {}
            )
        }
    }
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
                postCount = 1,
                onButtonClick = { /*TODO*/ },
                onFollowersClick = { /*TODO*/ },
                onFollowingClick = {},
                )
        }
    }
    
}










