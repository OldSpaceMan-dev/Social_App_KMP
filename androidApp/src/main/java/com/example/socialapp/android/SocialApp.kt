package com.example.socialapp.android

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.socialapp.android.common.components.AppBar
import com.example.socialapp.android.destinations.HomeDestination
import com.example.socialapp.android.destinations.LoginDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.utils.currentDestinationAsState

@Composable
fun SocialApp(
    //token: String?
    uiState: MainActivityUiState
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
            val currentUserId = (uiState as? MainActivityUiState.Success)?.currentUser?.id
            currentUserId?.let {
                // Только если currentUserId не равен null, вызываем AppBar
                AppBar(
                    navHostController = navHostController,
                    currentUserId = it
                )
            }

        },
        floatingActionButton = {


            //TODO ЛОГИРОВАНИЕ
            val currentDestination by navHostController.currentDestinationAsState()

            // Проверяем, что currentDestination равен HomeDestination.route
            val isVisible by remember {
                derivedStateOf { currentDestination?.route == HomeDestination.route }
            }

            LaunchedEffect(isVisible) {
                Log.d("Cureent_FloatingActionButton", "Cureent_Visible: $isVisible")
                Log.d("Home_FloatingActionButton", "Home_Visible: $HomeDestination")
                Log.d("Home_FloatingActionButton", "Bool_Visible: $isVisible")
                Log.d("FloatingActionButton", "Final__Visible: $isVisible, Destination: $currentDestination")
            }
            //TODO ЛОГИРОВАНИЕ



            AnimatedVisibility(
                visible = navHostController.currentDestinationAsState().value == HomeDestination,
                modifier = Modifier.padding(8.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        navHostController.navigate(
                            //вручнею перейдем к этому пути |||тк есть проблемы с провайдером библиоте ???
                            route = "create_post"
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary, // TODO надо проверить
                    //modifier = Modifier.animateContentSize()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }


    ) {innerPaddings ->

        //also need to create di
        DestinationsNavHost(
            modifier = Modifier.padding(innerPaddings),
            navGraph = NavGraphs.root,
            navController = navHostController
        )

    }




    when(uiState){
        MainActivityUiState.Loading -> {}
        is MainActivityUiState.Success -> {
            //Unit, что означает, что эффект будет запущен только один раз.
            LaunchedEffect(key1 = Unit) {
                if (uiState.currentUser.token.isEmpty()){
                    navHostController.navigate(LoginDestination.route){
                        //popUpTo - для удаления из стека назад до указанного пункта назначения.
                        popUpTo(HomeDestination.route){
                            inclusive = true //true,значает, что HomeDestination.route будет включен в удаление.
                        }
                    }
                }
            }
        }
    }







    /*
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
     */


    
}










