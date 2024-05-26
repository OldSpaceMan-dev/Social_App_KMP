package com.example.socialapp.common.util

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null // error message
){
    class Error<T>(data: T? = null, message: String): Result<T>(data, message) // extend Result and take data/message
    class Success<T>(data: T): Result<T>(data)



}