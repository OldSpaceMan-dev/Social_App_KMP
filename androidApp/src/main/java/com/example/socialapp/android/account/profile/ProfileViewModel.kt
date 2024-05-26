package com.example.socialapp.android.account.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.android.common.fake_data.Post
import com.example.socialapp.android.common.fake_data.Profile
import com.example.socialapp.android.common.fake_data.samplePosts
import com.example.socialapp.android.common.fake_data.sampleProfiles
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProfileViewModel: ViewModel(){

    var userInfoUiState by mutableStateOf(UserInfoUiState())
        private set

    var profilePostsUiState by mutableStateOf(ProfilePostsUiState())
        private set

    fun fetchProfile(userId: Int){
        viewModelScope.launch {

            delay(1000) // simulate net work request

            userInfoUiState = userInfoUiState.copy(
                isLoading = false,
                profile = sampleProfiles.find { it.id == userId } //find same id
            )

            profilePostsUiState = profilePostsUiState.copy(
                isLoading = false,
                posts = samplePosts
            )


        }
    }

}




data class UserInfoUiState(
    val isLoading: Boolean = true, // when we open -> show progress bar
    val profile: Profile? = null,
    val errorMessage: String? = null
)

data class ProfilePostsUiState(
    val isLoading: Boolean = true, // // when we open -> show progress bar
    val posts: List<Post> = listOf(),
    val errorMessage: String? = null
)