package com.example.socialapp.android.account.follows

import androidx.compose.runtime.Composable
import com.example.socialapp.android.destinations.ProfileDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun Followers(
    navigator: DestinationsNavigator,
    userId: Long
) {
    val viewModel: FollowsViewModel = koinViewModel()

    FollowsScreen(
        uiState = viewModel.uiState,
        fetchFollows = { viewModel.fetchFollows(userId, 1)},
        onItemClick = {
            navigator.navigate(ProfileDestination(it))
            //navigator.navigate(ProfileDestination(it))
        }
    )

}