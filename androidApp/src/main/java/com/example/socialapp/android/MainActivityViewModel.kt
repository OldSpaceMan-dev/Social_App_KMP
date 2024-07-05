package com.example.socialapp.android

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.common.data.local.UserSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainActivityViewModel(
    dataStore: DataStore<UserSettings> //DataStore используется для хранения и получения данных UserSettings.
): ViewModel() {
    //return flow string which will be our user token
    //val authState = dataStore.data.map { it.toAuthResultData().token }

    val uiState: StateFlow<MainActivityUiState> = dataStore.data.map {
        MainActivityUiState.Success(it)
    }.stateIn(
                        //Устанавливает область видимости корутины в viewModelScope,
                        // что гарантирует автоматическое отмену корутин, когда ViewModel уничтожается.
        scope = viewModelScope,
        initialValue = MainActivityUiState.Loading,
                        //поток должен начинать и останавливаться в зависимости от подписчиков.
                        //ожидать 5000 миллисекунд перед остановкой при отсутствии подписчиков
        started = SharingStarted.WhileSubscribed(5_000)
    )
}

//позволяет безопасно моделировать состояния с четким набором возможных значений.
sealed interface MainActivityUiState {
            //Singleton объект, что идеально подходит для состояний,ктр не требуют параметров.
    data object Loading : MainActivityUiState

    data class Success(val currentUser: UserSettings) : MainActivityUiState

}