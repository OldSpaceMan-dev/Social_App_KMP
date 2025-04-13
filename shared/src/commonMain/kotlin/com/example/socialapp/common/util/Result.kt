package com.example.socialapp.common.util

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null // error message
){

    class Error<T>(data: T? = null, message: String): Result<T>(data, message) // extend Result and take data/message
    class Success<T>(data: T): Result<T>(data)



}


//TODO надо подумать что бы айс мог получать Error and  Success -> top-level классы
/*
// Result.kt
sealed class Result<T>

data class Success<T>(val data: T) : Result<T>()

data class Error<T>(val message: String?, val data: T? = null) : Result<T>()

 */