package com.example.socialapp.android.common.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.socialapp.android.R

@Composable
fun CircleImage(
    modifier: Modifier = Modifier,
    url: String?,
    uri: Uri? = null,
    onClick: () -> Unit
) {
    AsyncImage(
        model = uri ?: url,
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() },
        placeholder = if (isSystemInDarkTheme()){
            painterResource(id = R.drawable.dark_image_place_holder)
        }else{
            painterResource(id = R.drawable.light_image_place_holder)
        },
        contentScale = ContentScale.Crop
    )
}