package com.example.socialapp.common.data.remote

import com.example.socialapp.common.data.model.LikeApiResponse
import com.example.socialapp.common.data.model.LikeParams
import com.example.socialapp.common.data.model.PostApiResponse
import com.example.socialapp.common.data.model.PostsApiResponse
import com.example.socialapp.common.util.Constants
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod


internal class PostApiService : KtorApi() {

    suspend fun getFeedPosts(
        userToken: String,
        currentUserId: Long,
        page: Int,
        pageSize: Int
    ): PostsApiResponse {
        val httpResponse = client.get {
            endPoint(path = "/posts/feed")
            parameter(key = Constants.CURRENT_USER_ID_PARAMETER, value = currentUserId)
            parameter(key = Constants.PAGE_QUERY_PARAMETER, value = page)
            parameter(key = Constants.PAGE_SIZE_QUERY_PARAMETER, value = pageSize)
            setToken(token = userToken)
        }
        return PostsApiResponse(code = httpResponse.status, data = httpResponse.body())
    }

    suspend fun likePost(
        userToken: String,
        likeParams: LikeParams
    ): LikeApiResponse {
        val httpResponse = client.post {
            endPoint(path = "/post/likes/add")
            setBody(likeParams)
            setToken(userToken)
        }
        return LikeApiResponse(code = httpResponse.status, data = httpResponse.body())
    }


    suspend fun unlikePost(
        userToken: String,
        likeParams: LikeParams
    ): LikeApiResponse {
        val httpResponse = client.delete {
            endPoint("/post/likes/remove")
            setBody(likeParams)
            setToken(userToken)
        }
        return LikeApiResponse(code = httpResponse.status, data = httpResponse.body())
    }

    suspend fun getUserPosts(
        userToken: String,
        userId: Long,
        currentUserId: Long,
        page: Int,
        pageSize: Int
    ): PostsApiResponse {
        val httpResponse = client.get {
            endPoint(path = "/posts/${userId}")
            parameter(key = Constants.CURRENT_USER_ID_PARAMETER, value = currentUserId)
            parameter(key = Constants.PAGE_QUERY_PARAMETER, value = page)
            parameter(key = Constants.PAGE_SIZE_QUERY_PARAMETER, value = pageSize)
            setToken(token = userToken)
        }

        return PostsApiResponse(code = httpResponse.status, data = httpResponse.body())
    }

    //single post
    suspend fun getPost(
        userToken: String,
        postId: Long,
        currentUserId: Long
    ): PostApiResponse {
        val httpResponse = client.get {
            endPoint(path = "/post/$postId")
            parameter(key = Constants.CURRENT_USER_ID_PARAMETER, value = currentUserId)
            setToken(token = userToken)
        }
        return PostApiResponse(code = httpResponse.status, data = httpResponse.body())
    }


    suspend fun createPost(
        userToken: String,
        newPostDate: String, //дополнительные данные для запроса нового поста - userid/caption
        imageBytes: ByteArray //само изображение
    ): PostApiResponse {
        val httpResponse = client.submitFormWithBinaryData( //post запрос на сервер
            formData = formData {//формат данных для отправки
                append(key = "post_data", value = newPostDate)
                append(
                    key = "post_images",
                    value = imageBytes,
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, value = "image/*")
                        //имя файла захардкожено post.jpg
                        append(HttpHeaders.ContentDisposition, value = "filename=post.jpg")
                    }
                )
            }
        ){
            endPoint(path = "/post/create")
            setToken(token = userToken)
            //добавит в соответствующий заголовок тип контента для мультиплатформенных данных
            setupMultipartRequest()
            method = HttpMethod.Post
        }
        return PostApiResponse(code = httpResponse.status, data = httpResponse.body())
    }



}
















