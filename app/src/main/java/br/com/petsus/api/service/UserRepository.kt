package br.com.petsus.api.service

import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.ChangePassword
import br.com.petsus.api.model.auth.ResetPassword
import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.api.model.base.EmptyResponse
import br.com.petsus.api.model.user.CreateUser
import br.com.petsus.api.model.user.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserRepository {
    @POST("user/reset-password")
    fun resetPassword(@Body body: ResetPassword): Call<EmptyResponse>

    @POST("user/change-password")
    fun changePassword(@Body body: ChangePassword): Call<EmptyResponse>

    @POST("user")
    fun createUser(@Body body: CreateUser): Call<AuthToken>

    @GET("user")
    fun getUser(): Call<BaseResponse<User>>
}