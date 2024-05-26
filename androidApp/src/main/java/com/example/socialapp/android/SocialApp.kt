package com.example.socialapp.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.socialapp.android.common.components.AppBar
import com.example.socialapp.android.destinations.HomeDestination
import com.example.socialapp.android.destinations.LoginDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun SocialApp(
    token: String?
) {

    val navHostController = rememberNavController()
    //val scaffoldState = rememberScaff
    val systemUiController = rememberSystemUiController()

    //icon for light/dak mode in appbar
    val isSystemInDark = isSystemInDarkTheme()
    val statusBarColor = if (isSystemInDark){
        MaterialTheme.colorScheme.surface
    }else{
        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f) // alpha - прозрачность ( - практически не прозрачен)
    }

    //SideEffect is used to apply a side effect
    // that changes the status bar color and icon color based on the current theme.
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = !isSystemInDark
        )
    }


    Scaffold(
        //?? need scaffoldState ??
        topBar = {
            AppBar(navHostController = navHostController)
        }


    ) {innerPaddings ->

        //also need to create di
        DestinationsNavHost(
            modifier = Modifier.padding(innerPaddings),
            navGraph = NavGraphs.root,
            navController = navHostController
        )

    }

    //нужно наблюдать за этим токеном всякий раз, когда
    // он меняется, нам нужно выполнить некоторую логику
    // если стартуя с HomeScreen - token = empty - выполним навигацию на LoginDestination

    LaunchedEffect(
        key1 = token,
        block = {
            if (token != null && token.isEmpty()){
                navHostController.navigate(LoginDestination.route){
                    //backStack
                    popUpTo(HomeDestination.route){
                        inclusive = true
                    }
                }
            }
        }
    )



    
}










