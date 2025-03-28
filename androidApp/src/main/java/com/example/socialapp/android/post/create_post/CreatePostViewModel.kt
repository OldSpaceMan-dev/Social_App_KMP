package com.example.socialapp.android.post.create_post

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.android.common.util.Event
import com.example.socialapp.android.common.util.EventBus
import com.example.socialapp.android.common.util.ImageBytesReader
import com.example.socialapp.common.util.Result
import com.example.socialapp.post.domain.usecase.CreatePostUseCase
import kotlinx.coroutines.launch


class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
    private val imageBytesReader: ImageBytesReader
): ViewModel(){

    var uiState by mutableStateOf(CreatePostUiState())
        private set


    //suspend - тк мы вызываем в viewModelScope.launch
    private suspend fun uploadPost(imageBytes: ByteArray) {
        val result = createPostUseCase(
            caption = uiState.caption,
            imageBytes = imageBytes
        )

        uiState = when (result) {
            is Result.Error -> {
                uiState.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
            }

            is Result.Success -> {
                EventBus.send(event = Event.PostCreated(post = result.data!!))
                uiState.copy(
                    isLoading = false,
                    postCreated = true
                )
            }
        }
    }



    private fun readImageBytes(imageUri: Uri){

        //show progressIndicator
        uiState = uiState.copy(
            isLoading = true
        )

        viewModelScope.launch {
            when (val result = imageBytesReader.readImageBytes(uri = imageUri)) {
                is Result.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    uploadPost(result.data!!)
                }
            }
        }


    }

    private fun onCaptionChange(input: String) {
        uiState = uiState.copy(caption = input)
    }


    fun onUiAction(action: CreatePostUiAction) {
        when(action) {
            is CreatePostUiAction.CaptionChanges -> {
                onCaptionChange(input = action.input)
            }
            is CreatePostUiAction.CreatePostAction -> {
                readImageBytes(imageUri = action.imageUri)
            }
        }
    }

}




data class CreatePostUiState(
    val caption: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val postCreated: Boolean = false
)


sealed interface CreatePostUiAction{

    data class CaptionChanges(val input: String): CreatePostUiAction

    class CreatePostAction(val imageUri: Uri): CreatePostUiAction
}