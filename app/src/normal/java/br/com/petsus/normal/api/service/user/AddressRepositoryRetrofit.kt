package br.com.petsus.normal.api.service.user

import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.api.model.address.CreateOrUpdateAddress
import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.api.model.base.EmptyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressRepositoryRetrofit {
    @GET("address")
    suspend fun list(): Response<BaseResponse<List<AppAddress>>>
    @DELETE("address/{id}")
    suspend fun delete(@Path(value = "id") id: Long): Response<EmptyResponse>
    @POST("address")
    suspend fun create(@Body address: CreateOrUpdateAddress): Response<BaseResponse<AppAddress>>
    @PUT("address")
    suspend fun update(@Body address: CreateOrUpdateAddress): Response<BaseResponse<AppAddress>>
}