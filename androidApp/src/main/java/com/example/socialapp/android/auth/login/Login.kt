package com.example.socialapp.android.auth.login

import androidx.compose.runtime.Composable
import com.example.socialapp.android.destinations.HomeDestination
import com.example.socialapp.android.destinations.LoginDestination
import com.example.socialapp.android.destinations.SignUpDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

// add @Destination - from library rav...

@Destination
@Composable
fun Login(
    navigator: DestinationsNavigator, // replace navController
) {

    val viewModel: LoginViewModel = koinViewModel()
    LoginScreen(
        uiState = viewModel.uiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onNavigateToHome = {
            navigator.navigate(HomeDestination){
                popUpTo(LoginDestination.route){
                    inclusive = true
                }
            }
        },
        onSignInClick = viewModel::signIn,
        onNavigateToSignup = {
            navigator.navigate(SignUpDestination){
                //remove destination from back stack
                // also remove LoginDestination from stack
                popUpTo(LoginDestination.route){
                    inclusive = true
                }
            }

        }
    )


}