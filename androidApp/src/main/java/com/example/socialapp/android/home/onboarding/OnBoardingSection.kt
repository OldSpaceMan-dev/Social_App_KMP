package com.example.socialapp.android.home.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.socialapp.android.R
import com.example.socialapp.android.common.fake_data.FollowsUser
import com.example.socialapp.android.common.fake_data.sampleUsers
import com.example.socialapp.android.common.theme.LargeSpacing
import com.example.socialapp.android.common.theme.MediumSpacing
import com.example.socialapp.android.common.theme.SocialAppTheme

@Composable
fun OnBoardingSection(
    modifier: Modifier = Modifier,
    users: List<FollowsUser>,
    onUserClick: (FollowsUser) -> Unit,
    onFollowButtonClick: (Boolean, FollowsUser) -> Unit,
    onBoardingFinish: () -> Unit, //button to dismiss onboarding
    ) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.onboarding_title),
            modifier = modifier
                .fillMaxWidth()
                .padding(top = MediumSpacing),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.oboarding_guidance_subtitle),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = LargeSpacing),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = modifier.heightIn(LargeSpacing))

        UsersRow(
            users = users,
            onUserClick = onUserClick,
            onFollowButtonClick = onFollowButtonClick
        )

        OutlinedButton(
            onClick = onBoardingFinish,
            modifier = modifier
                .fillMaxWidth(fraction = 0.5f)//half of the width
                .align(Alignment.CenterHorizontally)
                .padding(vertical = LargeSpacing),
            shape = RoundedCornerShape(percent = 50)
        ) {
            Text(text = stringResource(id = R.string.onboarding_button_text))
        }

    }
}


@Composable
fun UsersRow(
    modifier: Modifier = Modifier,
    users: List<FollowsUser>,
    onUserClick: (FollowsUser) -> Unit,
    onFollowButtonClick: (Boolean, FollowsUser) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(LargeSpacing),
        contentPadding = PaddingValues(horizontal = LargeSpacing),
        modifier = modifier
    ){
        items(
            items = users,
            key = {followUser -> followUser.id}
        ){
            OnBoardingUserItem(
                followsUser = it,
                onUserClick = onUserClick,
                onFollowButtonClick = onFollowButtonClick
            )
        }
    }

}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OnBoardingSectionPreview() {
    SocialAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            OnBoardingSection(
                users = sampleUsers,
                onUserClick = {},
                onFollowButtonClick = { _, _ ->},
                onBoardingFinish = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UsersRowPreview() {
    SocialAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            UsersRow(
                users = sampleUsers,
                onUserClick ={},
                onFollowButtonClick = { _, _ ->},
            )
        }
    }
}







