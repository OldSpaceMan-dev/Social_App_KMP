package com.example.socialapp.android.common.util

import android.util.Log
import com.example.socialapp.common.util.Result
import kotlinx.coroutines.delay


interface PagingManager<Model>{

    suspend fun loadItems()

    //reset the pagenation
    fun reset()

}


// оркестратор логики пагинации и коммуницировать с изменением состояния нашего view models

// private inline val  представляют собой функции
// callback function
class DefaultPagingManager<Model>( //we also call P Generic - Model just a name
    private inline val onRequest: suspend (page: Int) -> Result<List<Model>>, // лямбда функция
    private inline val onSuccess: (items: List<Model>, page: Int) -> Unit,
    private inline val onError: (cause: String, page: Int) -> Unit,
    private inline val onLoadStateChange: (isLoading: Boolean) -> Unit
): PagingManager<Model> {

    private var currentPage = Constants.INITIAL_PAGE_NUMBER
    private var isLoading = false

    override suspend fun loadItems() {
        Log.d("PagingManager", "Loading page $currentPage")
        if (isLoading) return
        isLoading = true // если не загрузились тогла true
        onLoadStateChange(true)

        delay(500)

        // get a page
        val result = onRequest(currentPage)
        //after we get page items we set
        isLoading = false
        onLoadStateChange(false)

        when(result){
            is Result.Error -> {
                //if null ?: we pass a UNEXPECTED_ERROR_MESSAGE
                onError(result.message ?: Constants.UNEXPECTED_ERROR_MESSAGE, currentPage)
            }

            is Result.Success -> {
                // !! -- значение не должно быть null
                // Если значение null, -> NullPointerException (NPE).
                onSuccess(result.data!!, currentPage)
                currentPage += 1
            }
        }


    }


    override fun reset() {
        currentPage = Constants.INITIAL_PAGE_NUMBER
    }

}


















