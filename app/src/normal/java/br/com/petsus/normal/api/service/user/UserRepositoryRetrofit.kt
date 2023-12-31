package br.com.petsus.normal.api.service.user

import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.ChangePassword
import br.com.petsus.api.model.auth.ResetPassword
import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.api.model.base.EmptyResponse
import br.com.petsus.api.model.user.CreateUser
import br.com.petsus.api.model.user.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface UserRepositoryRetrofit {
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPassword): Response<EmptyResponse>
    @POST("user/change-password")
    suspend fun changePassword(@Body body: ChangePassword): Response<EmptyResponse>
    @POST("auth/user")
    suspend fun createUser(@Body body: CreateUser): Response<BaseResponse<AuthToken>>
    @GET("user")
    suspend fun getUser(): Response<BaseResponse<User>>
    @PATCH("user")
    suspend fun update(@Body body: User): Response<EmptyResponse>
    @Multipart
    @POST("user/image")
    suspend fun saveImage(@Part file: MultipartBody.Part): Response<EmptyResponse>
}