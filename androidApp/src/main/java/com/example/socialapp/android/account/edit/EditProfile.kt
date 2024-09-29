package com.example.socialapp.android.account.edit

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun EditProfile(
    navigator: DestinationsNavigator,
    userId: Long
){
   val viewModel: EditProfileViewModel = koinViewModel()

    EditProfileScreen(
        editProfileUiState = viewModel.uiState,
        bioTextFieldValue = viewModel.bioTextFieldValue,

        onNameChange = viewModel::onNameChange,
        onBioChange = viewModel::onBioChange,
        userId = userId,
        onUiAction = viewModel::onUiAction,
        onUploadSucceed = { navigator.navigateUp() },

        //onUploadButtonClick = { viewModel.uploadProfile() },
        //fetchProfile = { viewModel.fetchProfile(userId) }
    )


}