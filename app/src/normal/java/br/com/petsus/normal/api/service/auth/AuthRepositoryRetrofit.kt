package br.com.petsus.normal.api.service.auth

import br.com.petsus.api.model.auth.AuthLogin
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.RefreshToken
import br.com.petsus.api.model.base.BaseResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRepositoryRetrofit {
    @POST("auth/login")
    suspend fun login(
        @Body body: AuthLogin
    ): Response<BaseResponse<AuthToken>>

    @POST("auth/refresh-token")
    suspend fun refreshToken(
        @Body body: RefreshToken
    ): Response<BaseResponse<AuthToken>>

    @POST("auth/refresh-token")
    fun refresh(
        @Body body: RefreshToken
    ): Call<BaseResponse<AuthToken>>
}