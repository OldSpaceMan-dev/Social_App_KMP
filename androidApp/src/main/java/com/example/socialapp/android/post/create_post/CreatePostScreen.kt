package com.example.socialapp.android.post.create_post

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.socialapp.android.R
import com.example.socialapp.android.common.components.ScreenLevelLoadingView
import com.example.socialapp.android.common.theme.ButtonHeight
import com.example.socialapp.android.common.theme.ExtraLargeSpacing
import com.example.socialapp.android.common.theme.LargeSpacing


@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    createPostUiState: CreatePostUiState,
    onPostCreated: () -> Unit,
    onUiAction: (CreatePostUiAction) -> Unit
) {

    //для ошибки
    val context = LocalContext.current

    //для выбора изображения из галереи
    //хранит урл изображения
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    // открывет окно выбора изображений и хранит выбранное изображение внутри selectedImage
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImage = uri }
    )


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.background
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
                .padding(ExtraLargeSpacing),
            verticalArrangement = Arrangement.spacedBy(LargeSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PostCaptionTextField(
                caption = createPostUiState.caption,
                onCaptionChange = {
                    onUiAction(CreatePostUiAction.CaptionChanges(it))
                }
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(LargeSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(id = R.string.select_post_image_label),
                    style = MaterialTheme.typography.labelSmall
                )

                selectedImage?.let {// если не null - те выбрали
                    AsyncImage( // покажем изобрадение которое выберем
                        model = it,
                        contentDescription = null,
                        modifier = modifier
                            .size(70.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                pickImage.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                        contentScale = ContentScale.Crop
                    )
                } ?: run {//если изображение не выбрано
                    //покажем заглужку изображения
                    IconButton(
                        onClick = {
                            pickImage.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        modifier = modifier
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.add_image_icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            //modifier = modifier.fillMaxSize()
                        )
                    }
                }

            }

            Button(
                onClick = {
                    selectedImage?.let {
                        onUiAction(CreatePostUiAction.CreatePostAction(it))
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp
                ),
                shape = MaterialTheme.shapes.medium,
                enabled = createPostUiState.caption.isNotBlank()
                        && !createPostUiState.isLoading
                        && selectedImage != null

            ) {
                Text(
                    text = stringResource(id = R.string.create_post_button_label)
                )
            }
        }


        if (createPostUiState.isLoading) {
            ScreenLevelLoadingView()
        }

    }

    if (createPostUiState.postCreated) {
        onPostCreated()
    }

    //если ошибка
    if (createPostUiState.errorMessage != null) {
        Toast.makeText(context, createPostUiState.errorMessage, Toast.LENGTH_SHORT).show()
    }


}


@Composable
private fun PostCaptionTextField(
    modifier: Modifier = Modifier,
    caption: String,
    onCaptionChange: (String) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        value = caption,
        onValueChange = onCaptionChange,
        colors = TextFieldDefaults.colors(
            //установка фона текстового поля, когда оно отключено
            disabledContainerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.surface
            }else{
                Color.Gray
            },
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.bodyMedium,

        // podskazka
        placeholder = {
            Text(
                text = stringResource(id = R.string.post_caption_hint),
                style = MaterialTheme.typography.bodyMedium
            )
        },

        shape = MaterialTheme.shapes.medium
    )



}


@Preview
@Composable
private fun CreatePostScreenPreview() {
    CreatePostScreen(
        createPostUiState = CreatePostUiState(caption = "Sample Text"),
        onPostCreated = { /*TODO*/ },
        onUiAction = {}
    )
    
}

























