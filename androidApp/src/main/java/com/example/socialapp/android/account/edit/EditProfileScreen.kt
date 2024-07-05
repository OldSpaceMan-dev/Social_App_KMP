package com.example.socialapp.android.account.edit

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.socialapp.android.R
import com.example.socialapp.android.common.components.CircleImage
import com.example.socialapp.android.common.components.CustomTextField
import com.example.socialapp.android.common.theme.ButtonHeight
import com.example.socialapp.android.common.theme.ExtraLargeSpacing
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.common.theme.SmallElevation


@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    editProfileUiState: EditProfileUiState,
    bioTextFieldValue: TextFieldValue,

    onNameChange: (String) -> Unit,
    onBioChange: (TextFieldValue) -> Unit,

    onUploadButtonClick: () -> Unit,
    onUploadSucceed: () -> Unit,
    fetchProfile: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        when{
            editProfileUiState.profile != null -> {
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

                    Box(){
                        CircleImage(
                            modifier = modifier.size(120.dp),
                            url = editProfileUiState.profile.profileUrl,
                            onClick = {}
                        )

                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = modifier
                                .align(Alignment.BottomEnd)
                                .shadow(
                                    elevation = SmallElevation,
                                    shape = RoundedCornerShape(percent = 25)
                                )
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(percent = 25)
                                )
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = modifier.height(LargeSpacing))

                    CustomTextField(
                        value = editProfileUiState.profile.name,
                        onValueChange = onNameChange,
                        hint = R.string.username_hint
                    )

                    BioTextField(
                        value = bioTextFieldValue,
                        onValueChange = onBioChange
                    )

                    Button(
                        onClick = {
                            onUploadButtonClick()
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .height(ButtonHeight),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = stringResource(id = R.string.upload_changes_text))
                    }
                }
            }


            // we check if profile is not null only we reach this case

            editProfileUiState.errorMessage != null -> {
                Column {
                    Text(
                        text = stringResource(id = R.string.could_not_load_profile),
                        style = MaterialTheme.typography.bodySmall.copy(
                            textAlign = TextAlign.Center
                        )
                    )

                    //retry user info
                    Button(
                        onClick = fetchProfile,
                        modifier = modifier.height(ButtonHeight),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = stringResource(id = R.string.retry_button_text))
                    }
                }
            }
        }


        if (editProfileUiState.isLoading){
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(key1 = Unit, block = {fetchProfile()})

    //if uploadSucceed or errorMessage change need to LaunchedEffect to run again
    LaunchedEffect(
        key1 = editProfileUiState.uploadSucceed,
        key2 = editProfileUiState.errorMessage,
        block = {
            if (editProfileUiState.uploadSucceed){
                //when navigate back
                onUploadSucceed()
            }
            if (editProfileUiState.profile != null && editProfileUiState.errorMessage != null){
                //we got error
                //we also check is profile not null - and we have error massage (upload new data)

                // massage is errorMassege
                // we show notification
                Toast.makeText(
                    context,
                    editProfileUiState.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    )

}


@Composable
fun BioTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        value = value,
        onValueChange = {
                onValueChange(
                    TextFieldValue(
                        text = it.text,
                        selection = TextRange(it.text.length)
                    )
                )
        },
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
                text = stringResource(id = R.string.user_bio_hint),
                style = MaterialTheme.typography.bodyMedium
            )
        },

        shape = MaterialTheme.shapes.medium
    )
}






























