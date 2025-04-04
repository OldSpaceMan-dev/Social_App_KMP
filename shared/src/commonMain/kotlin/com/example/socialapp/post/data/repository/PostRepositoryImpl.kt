package com.example.socialapp.post.data.repository

import com.example.socialapp.common.data.local.UserPreferences
import com.example.socialapp.common.data.local.UserSettings
import com.example.socialapp.common.data.model.LikeParams
import com.example.socialapp.common.data.model.NewPostParams
import com.example.socialapp.common.data.model.PostsApiResponse
import com.example.socialapp.common.data.model.RemotePostParams
import com.example.socialapp.common.data.remote.PostApiService
import com.example.socialapp.common.domain.model.Post
import com.example.socialapp.common.util.Constants
import com.example.socialapp.common.util.DispatcherProvider
import com.example.socialapp.common.util.Result
import com.example.socialapp.common.util.safeApiCall
import com.example.socialapp.post.domain.repository.PostRepository
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class PostRepositoryImpl(
    private val postApiService: PostApiService,
    private val userPreferences: UserPreferences,
    private val dispatcher: DispatcherProvider
) : PostRepository {


    override suspend fun getFeedPosts(page: Int, pageSize: Int): Result<List<Post>> {
        return fetchPosts(
            apiCall = {currentUserData ->
                postApiService.getFeedPosts(
                    userToken = currentUserData.token,
                    currentUserId = currentUserData.id,
                    page = page,
                    pageSize = pageSize
                )
            }
        )

        /* заменим ответом от фетчпост
        return withContext(dispatcher.io) {
            try {
                val userData = userPreferences.getUserData()
                val apiResponse = postApiService.getFeedPosts(
                    userToken = userData.token,
                    currentUserId = userData.id,
                    page = page,
                    pageSize = pageSize
                )

                when (apiResponse.code) {
                    HttpStatusCode.OK -> {
                        Result.Success(data = apiResponse.data.posts.map { it.toDomainPost() })
                    }

                    HttpStatusCode.BadRequest -> {
                        //Here you should format the message to make it understandable
                        //instead of passing what we get from the server (used for debugging)
                        Result.Error(message = "Error: ${apiResponse.data.message}")
                    }

                    else -> {
                        Result.Error(message = Constants.UNEXPECTED_ERROR)
                    }
                }
            } catch (ioException: IOException) {
                Result.Error(message = Constants.NO_INTERNET_ERROR)
            } catch (exception: Throwable) {
                Result.Error(message = "${exception.cause}")
            }
        }
        */
    }

    override suspend fun likeOrUnlikePost(postId: Long, shouldLike: Boolean): Result<Boolean> {
        return withContext(dispatcher.io) {
            try {
                val userData = userPreferences.getUserData()
                val likeParams = LikeParams(postId = postId, userId = userData.id)

                val apiResponse = if (shouldLike){
                    postApiService.likePost(userToken = userData.token , likeParams = likeParams)
                } else {
                    postApiService.unlikePost(userToken = userData.token, likeParams = likeParams)
                }

                if (apiResponse.code == HttpStatusCode.OK) {
                    Result.Success(data = apiResponse.data.success)
                } else {
                    Result.Error(data = false, message = "${apiResponse.data.message}")
                }
            } catch (ioException: IOException) {
                Result.Error(message = Constants.NO_INTERNET_ERROR)
            } catch (exception: Throwable) {
                Result.Error(message = "${exception.cause}")
            }
        }
    }


    override suspend fun getUserPosts(userId: Long, page: Int, pageSize: Int): Result<List<Post>> {
        return fetchPosts(
            apiCall = {currentUserData ->
                postApiService.getUserPosts(
                    userToken = currentUserData.token,
                    userId = userId,
                    currentUserId = currentUserData.id,
                    page = page,
                    pageSize = pageSize
                )
            }
        )
    }




    override suspend fun getPost(postId: Long): Result<Post> {
        return withContext(dispatcher.io) {
            try {
                val userData = userPreferences.getUserData()

                val apiResponse = postApiService.getPost(
                    userToken = userData.token,
                    postId = postId,
                    currentUserId = userData.id
                )

                if (apiResponse.code == HttpStatusCode.OK) {
                    Result.Success(
                        data = apiResponse.data.post!!.toDomainPost( isOwnPost = apiResponse.data.post.userId == userData.id)
                    )
                } else {
                    Result.Error(message = apiResponse.data.message!!)
                }

            }catch (ioException: IOException) {
                Result.Error(message = Constants.NO_INTERNET_ERROR)
            } catch (exception: Throwable) {
                Result.Error(message = "${exception.cause}")
            }
        }
    }



    override suspend fun createPost(
        caption: String,
        imageBytes: ByteArray
    ): Result<Post> {
        return safeApiCall(dispatcher) {
            val currentUserData = userPreferences.getUserData()

            //преобразем данные в json строку - отправляемую на сервер
            val postData = Json.encodeToString( //преобразует данные которые понятные беку
                serializer = NewPostParams.serializer(), // те данные которые будут с бека
                value = NewPostParams(
                    caption = caption,
                    userId = currentUserData.id
                )
            )

            val apiResponse = postApiService.createPost(
                userToken = currentUserData.token,
                newPostDate = postData,
                imageBytes = imageBytes
            )

            if (apiResponse.code == HttpStatusCode.OK) {
                Result.Success(
                    // сейчас мы возвращали только подстверждение загрузки
                    // теперь мы будет сразу без дополнительного запроса преобразовавыть данные для отправляемого поста
                    //+ изменим логику на беке - вернем данные поста вместе со статусом
                    //!!.toDomainPost() - разворачиваем данные в читаемый формат Android системой
                    data = apiResponse.data.post!!.toDomainPost( isOwnPost = apiResponse.data.post.userId == currentUserData.id)
                )
            } else {
                Result.Error(message = apiResponse.data.message ?: Constants.UNEXPECTED_ERROR)
            }

        }
    }


    override suspend fun removePost(postId: Long): Result<Post?> {
        return withContext(dispatcher.io) {

            try {
                val currentUserData = userPreferences.getUserData()

                val params = RemotePostParams(
                    postId = postId,
                    userId = currentUserData.id
                )

                val apiResponse = postApiService.removePost(
                    postParams = params,
                    userToken = currentUserData.token
                )

                if (apiResponse.code == HttpStatusCode.OK) {
                    val post = apiResponse.data.post?.toDomainPost(
                        isOwnPost = apiResponse.data.post.userId == currentUserData.id
                    )
                    Result.Success(data = post)
                } else {
                    Result.Error(message = apiResponse.data.message ?:Constants.UNEXPECTED_ERROR)
                }

            } catch (ioException: IOException) {
                Result.Error(message = Constants.NO_INTERNET_ERROR)
            } catch (exception: Throwable) {
                Result.Error(message = "${exception.cause}")
            }


        }
    }








    private suspend fun fetchPosts(
        apiCall: suspend (UserSettings) -> PostsApiResponse  // принимает UserSettings возвращает PostsApiResponse
    ): Result<List<Post>> {
        return withContext(dispatcher.io){
            try {
                val currentUserData = userPreferences.getUserData()
                val apiResponse = apiCall(currentUserData)

                when (apiResponse.code) {
                    HttpStatusCode.OK -> {
                        Result.Success(
                            data = apiResponse.data.posts.map {
                                it.toDomainPost( isOwnPost =  it.userId == currentUserData.id)
                            }
                        )
                    }
                    else -> {
                        Result.Error(message = Constants.UNEXPECTED_ERROR)
                    }
                }
            }catch (ioException: IOException) {
                Result.Error(message = Constants.NO_INTERNET_ERROR)
            } catch (exception: Throwable) {
                Result.Error(message = "${exception.cause}")
            }
        }
    }





}


















