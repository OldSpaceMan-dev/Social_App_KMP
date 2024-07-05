package com.example.socialapp.android.account.follows

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.socialapp.android.common.components.CircleImage
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.common.theme.MediumSpacing

@Composable
fun FollowsListItem(
    modifier: Modifier = Modifier,
    name: String,
    bio: String,
    imageUrl: String,
    onItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(
                horizontal = LargeSpacing,
                vertical = MediumSpacing
            ),
        horizontalArrangement = Arrangement.spacedBy(LargeSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {

        CircleImage(
            modifier = modifier.size(40.dp),
            url = imageUrl,
            onClick = {}
        )

        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = bio,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}












