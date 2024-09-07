package com.example.socialapp.android.account.follows

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.socialapp.android.account.profile.ProfileUiAction
import com.example.socialapp.android.common.components.loadingMoreItem
import com.example.socialapp.android.common.fake_data.sampleUsers
import com.example.socialapp.android.common.theme.SocialAppTheme


@Composable
fun FollowsScreen(
    modifier: Modifier = Modifier,
    uiState: FollowsUiState,


    userId: Long,
    followsType: Int,
    onUiAction: (FollowsUiAction) -> Unit,
    onProfileNavigation: (userId: Long) -> Unit

    //fetchFollows: () -> Unit,//calback to inform viewModel when it's ready to draw to view
    //onItemClick: (Long) -> Unit   //pass to userId -> to navigate to profile screen
) {

    //Создает и запоминает состояние списка
    val listState = rememberLazyListState()

    //paginashion - show last post and reload new post
    val shouldFetchMoreFollows by remember {
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
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState
        ){
            items(
                items = uiState.followsUsers,
                key = { user -> user.id }
            ){
                FollowsListItem(
                    name = it.name,
                    bio = it.bio,
                    imageUrl = it.imageUrl
                ) {
                    onProfileNavigation(it.id)
                }
            }

            //загружается и есть загруженные пользователи
            if (uiState.isLoading && uiState.followsUsers.isNotEmpty() && !uiState.endReached){
                loadingMoreItem() // "кастомный" спинер
            }

        }
        // Показать индикатор загрузки, если список пуст и данные загружаются
        if (uiState.isLoading && uiState.followsUsers.isEmpty()){
            CircularProgressIndicator()
        }



        // Показать сообщение, если список пуст и загрузка завершена
        if (!uiState.isLoading && uiState.followsUsers.isEmpty()) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (followsType == 1) {
                    Text(
                        text = "You don't have any followers.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    Text(
                        text = "You're not following anyone.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }


    }

    LaunchedEffect(
        key1 = Unit,
        block = {
            onUiAction(FollowsUiAction.FetchFollowsAction(userId = userId, followsType = followsType))}
    )


    //send request to load more post
    LaunchedEffect(key1 = shouldFetchMoreFollows) {
        if (shouldFetchMoreFollows && !uiState.endReached){
            onUiAction(FollowsUiAction.LoadMoreFollowsAction)
        }
    }

}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FollowsScreenPreview() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            FollowsScreen(
                uiState = FollowsUiState(
                    //samplePosts.first().toDomainPost()
                    isLoading = false,
                    followsUsers = sampleUsers.map { it.toFollowsUser() }
                ),
                userId = 1,
                followsType = 1,
                onUiAction = {},
                onProfileNavigation = {}
            )
        }
    }
}



















