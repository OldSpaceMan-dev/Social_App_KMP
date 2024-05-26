package com.example.socialapp.android.auth.singup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.android.common.datastore.UserSettings
import com.example.socialapp.android.common.datastore.toUserSettings
import com.example.socialapp.auth.domain.usecase.SignUpUseCase
import com.example.socialapp.common.util.Result
import kotlinx.coroutines.launch

class SignUpViewModel(
    // instance - signUp use case
    private val signUpUseCase: SignUpUseCase,
    //data store instance
    private val dataStore: DataStore<UserSettings>
): ViewModel() {

    var uiState by mutableStateOf(SingUpUiState())
        private set

    fun signUp(){
        // for new user - coroutine
        viewModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true) // show circular progr indicator

            val authResultData = signUpUseCase(
                uiState.username,
                uiState.email,
                uiState.password
            )

            uiState = when(authResultData){
                is Result.Error -> {
                    uiState.copy(
                        isAuthenticating = false, // stop circular indicator
                        authErrorMessage = authResultData.message
                    )
                }
                is Result.Success -> {
                    //!! принудительно разворачиваю это, потому что всякий раз,
                    // когда результат успешен
                    // мы уверены, что поток данных здесь никогда не будет нулевым
                    dataStore.updateData {
                        authResultData.data!!.toUserSettings()
                    }
                    uiState.copy(
                        isAuthenticating = false,
                        authenticationSucceed = true
                    )
                }
            }

        }
    }

    fun updateUsername(input: String){
        uiState = uiState.copy(username = input) // update ui state
    }

    fun updateEmail(input: String){
        uiState = uiState.copy(email = input) // update ui state
    }

    fun updatePassword(input: String){
        uiState = uiState.copy(password = input) // update ui state
    }



}

data class SingUpUiState(
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var isAuthenticating: Boolean = false, //show progress indicator
    var authErrorMessage: String? = null,
    var authenticationSucceed: Boolean = false // trigger navigat to Home funct(method)
)