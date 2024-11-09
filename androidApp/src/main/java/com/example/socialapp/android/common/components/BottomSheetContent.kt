package com.example.socialapp.android.common.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.common.theme.SocialAppTheme
import com.example.socialapp.android.common.util.toCurrentUrl


@Composable
fun MoreActionsBottomSheetContent(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String?,
    actions: List<SheetAction>
) {

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = modifier.padding(all = LargeSpacing)
        )

        Divider()


        ListItem(
            modifier = modifier,
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingContent = {
                CircleImage(
                    url = imageUrl?.toCurrentUrl(),
                    modifier = modifier.size(25.dp),
                    onClick = {}
                )
            }
        )


        actions.forEach { action ->
            ListItem(
                modifier = modifier
                    .clickable(enabled = action.enabled, onClick = action.onClick)
                    .graphicsLayer { alpha = if (action.enabled) 1f else 0.4f },
                headlineContent = {
                    Text(
                        text = action.label,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = null
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }



}


data class SheetAction(
    val label: String,
    val icon: ImageVector,
    val enabled: Boolean = true,
    val onClick: () -> Unit
)


@Preview
@Composable
private fun MoreActionsBottomSheetContentPreview() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            val  action = listOf(
                SheetAction(
                    label = "ViewProfile",
                    icon = Icons.Default.Person,
                    onClick = {}
                ),
                SheetAction(
                    label = "Delete Post",
                    icon = Icons.Default.Delete,
                    onClick = {}
                )
            )
            MoreActionsBottomSheetContent(
                title = "Post",
                imageUrl = "",
                actions = action
            )
        }
    }
}