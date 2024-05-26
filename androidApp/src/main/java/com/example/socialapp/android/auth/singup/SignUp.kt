package com.example.socialapp.android.auth.singup

import androidx.compose.runtime.Composable
import com.example.socialapp.android.destinations.HomeDestination
import com.example.socialapp.android.destinations.LoginDestination
import com.example.socialapp.android.destinations.SignUpDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun SignUp(
    navigator: DestinationsNavigator
) {
    val viewModel: SignUpViewModel = koinViewModel()

    SignUpScreen(
        uiState = viewModel.uiState,
        onUsernameChange = viewModel::updateUsername,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onNavigateToLogin = {
            navigator.navigate(LoginDestination) { //create LoginDestination uses add liblary*
                popUpTo(SignUpDestination.route){
                    inclusive = true
                }
            }
        },
        onNavigateToHome = {
            navigator.navigate(HomeDestination){
                //remove all destination from back stack
                // also remove SignUpDestination from stack
                popUpTo(SignUpDestination.route){
                    inclusive = true
                }
            }
        },
        onSignUpClick = viewModel::signUp
    )


}