package com.example.socialapp.android.common.components


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.socialapp.android.R
import com.example.socialapp.android.account.profile.ProfileScreen
import com.example.socialapp.android.account.profile.ProfileUiAction
import com.example.socialapp.android.account.profile.ProfileViewModel
import com.example.socialapp.android.common.theme.SmallElevation
import com.example.socialapp.android.destinations.EditProfileDestination
import com.example.socialapp.android.destinations.FollowersDestination
import com.example.socialapp.android.destinations.FollowingDestination
import com.example.socialapp.android.destinations.HomeDestination
import com.example.socialapp.android.destinations.LoginDestination
import com.example.socialapp.android.destinations.PostDetailDestination
import com.example.socialapp.android.destinations.ProfileDestination
import com.example.socialapp.android.destinations.SignUpDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    currentUserId: Long
) {
    val currentDestination = navHostController.currentDestinationAsState().value

    Surface(
        modifier = modifier,
        shadowElevation = SmallElevation
    ) {
        TopAppBar(
            title = {
                //find the route of destination
                Text(
                    text = stringResource(id = getAppBarTitle(currentDestination?.route))
                )

            },
            modifier = modifier,

            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.outlineVariant
            ),
            actions = {
                AnimatedContent(targetState = currentDestination?.route,
                    label = ""
                ) { destinationRoute ->
                    if (destinationRoute == HomeDestination.route) {
                        IconButton(onClick = {
                            // тут навигация на мой профиль
                            navHostController.navigate(ProfileDestination(currentUserId))
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.person_circle_icon),
                                contentDescription = null
                            )
                        }
                    } else if (shouldShowNavigationIcon(destinationRoute)) {
                        IconButton(onClick = {
                            // Переход на главный экран
                            navHostController.navigate(HomeDestination.route) {
                                popUpTo(HomeDestination.route) { inclusive = true }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.home_icon), // Добавьте свой ресурс иконки дома
                                contentDescription = stringResource(id = R.string.home_destination_title)
                            )
                        }
                    }

                    
                }
                      
                
                /*
                //show icon profile on HomeScreen
                AnimatedVisibility(
                    visible = currentDestination?.route == HomeDestination.route
                ) {
                    IconButton(onClick = {
                        // тут навигация на мой профиль
                        navHostController.navigate(ProfileDestination(currentUserId))
                        //navHostController.navigate(ProfileDestination.route ) // + "/${currentUserId}"
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.person_circle_icon),
                            contentDescription = null
                        )
                    }
                }
                // Иконка дома показывается на всех экранах, кроме главного
                if (shouldShowNavigationIcon(currentDestination?.route)) {
                    IconButton(onClick = {
                        // Переход на главный экран
                        navHostController.navigate(HomeDestination.route) {
                            popUpTo(HomeDestination.route) { inclusive = true }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.home_icon), // Добавьте свой ресурс иконки дома
                            contentDescription = stringResource(id = R.string.home_destination_title)
                        )
                    }
                }
                
                 */

            },

            navigationIcon = {
                // if shouldShowNavigationIcon = true - те условие исключает экраны в fun
                // -> show back icon - and Up navHost
                if (shouldShowNavigationIcon(currentDestination?.route)){
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_arrow_back),
                            contentDescription = null
                        )
                    }
                }else {
                    null
                }
            }
        )
    }
}

// Int - >  we return string res for title

//build title for Destination
private fun getAppBarTitle(currentDestinationRoute: String?): Int{
    return when(currentDestinationRoute){
        LoginDestination.route -> R.string.login_destination_title
        SignUpDestination.route -> R.string.signup_destination_title
        HomeDestination.route -> R.string.home_destination_title
        PostDetailDestination.route -> R.string.post_detail_destination_title
        ProfileDestination.route -> R.string.profile_destination_title
        EditProfileDestination.route -> R.string.edit_profile_destination_title
        FollowingDestination.route -> R.string.following_text
        FollowersDestination.route -> R.string.followers_text
        else -> R.string.no_destination_title
    }
}


//if need back navigation icon
fun shouldShowNavigationIcon(currentDestinationRoute: String?): Boolean{
    return !(
            currentDestinationRoute == LoginDestination.route ||
            currentDestinationRoute == SignUpDestination.route ||
            currentDestinationRoute == HomeDestination.route
            )
}


@Preview
@Composable
private fun AppBarPreview() {
    val navController = rememberNavController()
    //val currentDestination = navController

    //AppBar(navHostController = navController)
}








