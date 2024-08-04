package com.example.socialapp.android.account.follows

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun FollowsScreen(
    modifier: Modifier = Modifier,
    uiState: FollowsUiState,
    fetchFollows: () -> Unit,//calback to inform viewModel when it's ready to draw to view
    onItemClick: (Long) -> Unit   //pass to userId -> to navigate to profile screen
) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ){
            items(
                items = uiState.sampleFollowsUsers
                    .map { it.toFollowsUser() },
                key = { user -> user.id }
            ){
                FollowsListItem(
                    name = it.name,
                    bio = it.bio,
                    imageUrl = it.imageUrl ?: ""
                ) {
                    onItemClick(it.id)
                }
            }
        }

        if (uiState.isLoading && uiState.sampleFollowsUsers.isEmpty()){
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(key1 = Unit, block = { fetchFollows() })

}