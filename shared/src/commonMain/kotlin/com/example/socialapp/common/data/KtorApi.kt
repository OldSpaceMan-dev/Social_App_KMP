package com.example.socialapp.common.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json




private const val BASE_URL = "http://192.168.1.138:8080/"

internal abstract class KtorApi {
    //instantiate and configure our HTTP client object

    val client = HttpClient {
        install(ContentNegotiation){
            //axialiazition iblir
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    //extension method call endPoint
    fun HttpRequestBuilder.endPoint(path: String){
        url {
            takeFrom(BASE_URL)
            path(path)
            contentType(ContentType.Application.Json)
        }
    }

}