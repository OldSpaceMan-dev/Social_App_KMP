package com.example.socialapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.socialapp.android.common.theme.SocialAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocialAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //всякий раз, когда токен известен, мы ничего не будем делать
                    // до тех пор, пока токен не станет либо value, либо пустым
                    val token = viewModel.authState.collectAsStateWithLifecycle(
                        initialValue = null
                    )
                    SocialApp(token.value)
                }
            }
        }
    }
}


@Preview
@Composable
fun DefaultPreview() {
    SocialAppTheme {
    }
}


