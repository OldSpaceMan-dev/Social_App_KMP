package com.example.socialapp.android.account.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.android.common.fake_data.Profile
import com.example.socialapp.android.common.fake_data.sampleProfiles
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditProfileViewModel : ViewModel(){
    var uiState: EditProfileUiState by mutableStateOf(EditProfileUiState())
        private set

    //variable for bio text field
    var bioTextFieldValue: TextFieldValue by mutableStateOf(TextFieldValue(text = ""))
        private set

    fun fetchProfile(userId: Long){
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            delay(1000)

            uiState = uiState.copy(
                isLoading = false,
                profile = sampleProfiles.find { it.id == userId.toInt() }
            )

            //modified bio textFeild
            bioTextFieldValue = bioTextFieldValue.copy(
                text = uiState.profile?.bio ?: "",
                // точка курсора на тексте - выделение
                selection = TextRange(index = uiState.profile?.bio?.length ?: 0)
            )

        }
    }

    fun uploadProfile(){
        viewModelScope.launch{
            uiState = uiState.copy(isLoading = true)

            delay(1000)

            uiState = uiState.copy(
                isLoading = false,
                uploadSucceed = true // automatically navig back to profile screen
            )
        }
    }


    fun onNameChange(inputName: String){
        uiState = uiState.copy(
            profile = uiState.profile?.copy(name = inputName)
        )
    }

    fun onBioChange(inputBio: TextFieldValue){
        bioTextFieldValue = bioTextFieldValue.copy(
            text = inputBio.text,
            selection = TextRange(index = inputBio.text.length)
        )
    }



}





data class EditProfileUiState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val uploadSucceed: Boolean = false,
    val errorMessage: String? = null
)










