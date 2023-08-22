package br.com.petsus.normal.api.service.clinic

import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.api.model.clinic.Clinic
import br.com.petsus.api.model.clinic.ClinicAddress
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ClinicRepositoryRetrofit {
    @GET("clinic/{id}")
    suspend fun find(
        @Path(value = "id") id: Long
    ): Response<BaseResponse<Clinic>>

    @GET("clinic/near")
    suspend fun list(
        @Query(value = "lat") lat: Double,
        @Query(value = "lng") lng: Double,
        @Query(value = "distance") distance: Double
    ): Response<BaseResponse<List<ClinicAddress>>>
}