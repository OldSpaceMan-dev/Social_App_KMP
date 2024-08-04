package com.example.socialapp.android.home.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialapp.android.R
import com.example.socialapp.android.common.components.CircleImage
import com.example.socialapp.android.common.components.FollowButton
import com.example.socialapp.android.common.fake_data.sampleUsers
import com.example.socialapp.android.common.theme.MediumSpacing
import com.example.socialapp.android.common.theme.SmallSpacing
import com.example.socialapp.android.common.theme.SocialAppTheme
import com.example.socialapp.android.common.util.toCurrentUrl
import com.example.socialapp.common.domain.model.FollowsUser


@Composable
fun OnBoardingUserItem(
    modifier: Modifier = Modifier,
    followsUser: FollowsUser,

    onUserClick: (FollowsUser) -> Unit, //navigate to profile screen
    isFollowing: Boolean = false,
    onFollowButtonClick: (Boolean, FollowsUser) -> Unit,

    ) {
    Card(
        modifier = modifier
            .height(140.dp)
            .width(130.dp)
            .clickable { onUserClick(followsUser) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(MediumSpacing),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircleImage(
                modifier = modifier.size(50.dp),
                url = followsUser.imageUrl?.toCurrentUrl(),
                onClick = {onUserClick(followsUser)}
            )

            Spacer(modifier = modifier.height(SmallSpacing))

            Text(
                text = followsUser.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = modifier.height(MediumSpacing))

            FollowButton(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(30.dp),

                text = if (!followsUser.isFollowing) {
                    R.string.follow_text_label
                } else R.string.unfollow_text_label,

                onClick = { onFollowButtonClick(!isFollowing, followsUser) },
                isOutlined = followsUser.isFollowing
            )

        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OnBoardingUserItemPreview() {
    SocialAppTheme {
        OnBoardingUserItem(
            followsUser = sampleUsers.first().toFollowsUser(),
            onUserClick ={},
            onFollowButtonClick = { _ ,_ -> }
        )
    }
}










