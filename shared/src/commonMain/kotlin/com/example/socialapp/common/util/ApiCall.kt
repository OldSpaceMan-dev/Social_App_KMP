package com.example.socialapp.common.util

import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext


internal suspend fun <T> safeApiCall(
    dispatcher: DispatcherProvider,
    errorHandler: (Throwable) -> Result<T> = { defaultErrorHandler(it) },
    apiCall: suspend () -> Result<T>
): Result<T> = withContext(dispatcher.io) {
    try {
        apiCall()
    } catch (exception: Throwable) {
        if (exception is CancellationException) throw exception
        errorHandler(exception)
    }
}


// Эта функция представляет собой простую утилиту
//, которая преобразует исключения в наш результат - Объект Error.
private fun <T> defaultErrorHandler(error: Throwable): Result<T>{
    return if (error is IOException){
        Result.Error(message = Constants.NO_INTERNET_ERROR)
    } else {
        Result.Error(message = error.message ?: Constants.UNEXPECTED_ERROR)
    }
}