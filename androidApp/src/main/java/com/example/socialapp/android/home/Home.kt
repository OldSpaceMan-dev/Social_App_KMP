package com.example.socialapp.android.home

import androidx.compose.runtime.Composable
import com.example.socialapp.android.destinations.PostDetailDestination
import com.example.socialapp.android.destinations.ProfileDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination(start = true)
fun Home(
    navigator: DestinationsNavigator
) {

    val viewModel: HomeScreenViewModel = koinViewModel()

    HomeScreen(
        onBoardingUiState = viewModel.onBoardingUiState,
        postsFeedUiState = viewModel.postsFeedUiState,
        homeRefreshState = viewModel.homeRefreshState,
        onUiAction = {viewModel.onUiAction(it)},
        onProfileNavigation = {navigator.navigate(ProfileDestination(it))},
        onPostDetailNavigation = {navigator.navigate(PostDetailDestination(it.postId))}
    )

}