package com.example.socialapp.android.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.common.data.local.UserSettings
import com.example.socialapp.common.data.local.toUserSettings
import com.example.socialapp.auth.domain.usecase.SignInUseCase
import com.example.socialapp.common.util.Result
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    //private val dataStore: DataStore<UserSettings> //data store instance
): ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun signIn(){

        viewModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true)

            val authResultData = signInUseCase(
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

                    //update data store. takes block which will apply all changes
                    // that is made to user settings

                    // after log in / signIn viewModel - update dataStore
                    /* перенесли запись данных на уроверь shared
                    dataStore.updateData {
                        authResultData.data!!.toUserSettings() // use mapper func
                    }
                     */


                    uiState.copy(
                        isAuthenticating = false,
                        authenticationSucceed = true
                    )
                }
            }
        }
    }



    fun updateEmail(input: String){
        uiState = uiState.copy(email = input) // update ui state
    }

    fun updatePassword(input: String){
        uiState = uiState.copy(password = input) // update ui state
    }



}

data class LoginUiState(
    var email: String = "",
    var password: String = "",
    var isAuthenticating: Boolean = false, //show progress indicator
    var authErrorMessage: String? = null,
    var authenticationSucceed: Boolean = false // trigger navigat to Home funct(method)
)


















