package com.example.socialapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.socialapp.android.common.theme.SocialAppTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        //создает и возвращает объект SplashScreen для управления экраном заставки.
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    // Применяем оператор onEach, который обрабатывает каждое выданное значение потока.
                    .onEach {
                        // Внутри onEach мы обновляем uiState текущим значением, выданным из потока.
                        // Эта лямбда-функция является коллектором, используемым методом collect().
                        uiState = it
                    }
                    // Вызываем collect() на потоке, чтобы подписаться на его значения
                    // и передать коллектор (лямбда-функция из onEach) для обработки выданных значений.
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition{
            // Возвращаем true, если текущее состояние UI - Loading.
            // Это означает, что экран заставки будет оставаться на экране, пока приложение не загрузится полностью.
            uiState == MainActivityUiState.Loading
        }

        setContent {
            SocialAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SocialApp(uiState = uiState)
                    /*
                    //всякий раз, когда токен известен, мы ничего не будем делать
                    // до тех пор, пока токен не станет либо value, либо пустым
                    val token = viewModel.authState.collectAsStateWithLifecycle(
                        initialValue = null
                    )
                    SocialApp(token.value)
                     */
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