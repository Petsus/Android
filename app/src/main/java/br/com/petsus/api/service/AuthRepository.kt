package br.com.petsus.api.service

import br.com.petsus.api.model.auth.AuthLogin
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.RefreshToken
import br.com.petsus.api.model.base.BaseResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRepository {
    @POST("auth/login")
    fun login(@Body body: AuthLogin): Call<BaseResponse<AuthToken>>

    @POST("auth/refresh-token")
    fun refreshToken(@Body body: RefreshToken): Call<BaseResponse<AuthToken>>?
}