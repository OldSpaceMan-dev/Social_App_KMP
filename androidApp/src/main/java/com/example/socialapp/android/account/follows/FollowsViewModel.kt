package com.example.socialapp.android.account.follows

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.android.common.util.Constants
import com.example.socialapp.android.common.util.DefaultPagingManager
import com.example.socialapp.android.common.util.PagingManager
import com.example.socialapp.common.domain.model.FollowsUser
import com.example.socialapp.follows.domain.usecase.GetFollowsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FollowsViewModel(
    private val getFollowsUseCase: GetFollowsUseCase
) : ViewModel(){

    var uiState by mutableStateOf(FollowsUiState())
        private set


    private lateinit var pagingManager: PagingManager<FollowsUser>

    // followsType - if = 1  we need to fetch(извлечь) this user followers
    // if = 2 this user following
    // 1 - followers // 2 - following
    private fun fetchFollows(userId: Long, followsType: Int){
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            delay(1000)

            //если менежддер пагинации не инициализирован - вызовем условие
            if (!::pagingManager.isInitialized){
                pagingManager = createPagingManager(
                    userId = userId,
                    followsType = followsType
                )
                //далее вызывает для инициализации дозагрузки
                pagingManager.loadItems()
            }
        }
    }

    private fun createPagingManager(userId: Long, followsType: Int): PagingManager<FollowsUser> {
        return DefaultPagingManager(
            onRequest = { page ->
                getFollowsUseCase(
                    userId = userId,
                    page = page,
                    pageSize = Constants.DEFAULT_REQUEST_PAGE_SIZE,
                    followsType = followsType
                )
            },
            onSuccess = {follows, _ ->
                //проверка на уникальность id? удаляем дубли
                val uniqueFollows = (uiState.followsUsers + follows).distinctBy { it.id }

                uiState = uiState.copy(
                    isLoading = false,
                    followsUsers = uniqueFollows,//uiState.followsUsers + follows,
                    //если true - то с сервака пришло меньше чем константа
                    //false если с сервака пришло БОЛЬШЕ чем константа
                    endReached = follows.size < Constants.DEFAULT_REQUEST_PAGE_SIZE

                )
            },
            onError = { message, _ ->
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = message
                )
            },
            onLoadStateChange = {//isLoading ->
                uiState = uiState.copy(
                    isLoading = it//isLoading
                )
            }
        )
    }



    private fun loadMoreFollows() {
        Log.d("PagingManagerFollows", "loadMoreFollows false")
        if (uiState.endReached) return

        viewModelScope.launch {
            Log.d("PagingManagerFollows", "loadMoreFollows true")
            pagingManager.loadItems()
        }

    }




    fun onUiAction(action: FollowsUiAction) {
        when(action) {
            is FollowsUiAction.FetchFollowsAction -> {
                fetchFollows(action.userId, action.followsType)
            }
            is FollowsUiAction.LoadMoreFollowsAction -> {
                loadMoreFollows()
            }

        }
    }


}

data class FollowsUiState(
    val isLoading: Boolean = false,
    val followsUsers: List<FollowsUser> = listOf(),
    val errorMessage: String? = null,
    val endReached: Boolean = false
)



sealed interface FollowsUiAction{
    data class FetchFollowsAction(val userId: Long, val followsType: Int): FollowsUiAction

    data object LoadMoreFollowsAction: FollowsUiAction
}


































