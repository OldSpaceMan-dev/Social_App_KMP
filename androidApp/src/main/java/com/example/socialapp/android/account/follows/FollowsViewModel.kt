package com.example.socialapp.android.account.follows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.android.common.fake_data.FollowsUser
import com.example.socialapp.android.common.fake_data.sampleUsers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FollowsViewModel : ViewModel(){

    var uiState by mutableStateOf(FollowsUiState())
        private set


    // followsType - if = 1  we need to fetch(извлечь) this user followers
    // if = 2 this user following
    // 1 - followers // 2 - following
    fun fetchFollows(userId: Int, followsType: Int){
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            delay(1000)

            //add logic on backend side
            uiState = uiState.copy(
                isLoading = false,
                followsUsers = sampleUsers
            )
        }

    }



}

data class FollowsUiState(
    val isLoading: Boolean = false,
    val followsUsers: List<FollowsUser> = listOf(),
    val errorMessage: String? = null
)