package com.example.socialapp.android.common.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialapp.android.R
import com.example.socialapp.android.common.fake_data.Comment
import com.example.socialapp.android.common.fake_data.sampleComments
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.common.theme.MediumSpacing
import com.example.socialapp.android.common.theme.SocialAppTheme

@Composable
fun CommentListItem(
    modifier: Modifier = Modifier,
    comment: Comment,
    onProfileClick: (Int) -> Unit,
    onMoreIconClick: () -> Unit,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(LargeSpacing),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
    ) {

        CircleImage(
            imageUrl = comment.authorImageUrl,
            modifier = modifier.size(30.dp)
        ) {
            onProfileClick(comment.authorId)
        }

        Column(
            modifier.weight(1f)
        ) {
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
            ) {
                Text(
                    text = comment.authorName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = modifier
                        .alignByBaseline()
                )

                Text(
                    text = comment.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = modifier
                        .alignByBaseline()
                        .weight(1f) //Расположите элемент вертикально таким образом,
                                            // чтобы его первая базовая линия совпадала с родственными элементами,
                )

                Icon(
                    painter = painterResource(id = R.drawable.round_more_horizontal),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = modifier.clickable { onMoreIconClick() }
                )


            }
            
            Text(
                text = comment.comment,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CommentListItemPreview() {
    SocialAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            CommentListItem(
                comment = sampleComments.first(),
                onProfileClick = {},
                onMoreIconClick = {}
            )
        }
    }
}








