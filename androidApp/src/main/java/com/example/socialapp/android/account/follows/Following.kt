package com.example.socialapp.android.account.follows

import androidx.compose.runtime.Composable
import com.example.socialapp.android.destinations.ProfileDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun Following(
    navigator: DestinationsNavigator,
    userId: Long,

) {
    val viewModel: FollowsViewModel = koinViewModel()

    FollowsScreen(
        uiState = viewModel.uiState,

        userId = userId,
        followsType = 2,
        onUiAction = viewModel::onUiAction,
        onProfileNavigation = { navigator.navigate(ProfileDestination(it)) },
        //fetchFollows = { viewModel.fetchFollows(userId, 2) },
        /*onItemClick ={
            navigator.navigate(ProfileDestination(it))
        }
         */
    )

}