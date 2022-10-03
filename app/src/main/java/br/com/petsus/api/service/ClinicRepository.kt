package br.com.petsus.api.service

import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.api.model.clinic.Clinic
import br.com.petsus.api.model.clinic.ClinicAddress
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ClinicRepository {
    @GET("clinic/near")
    fun allClinic(@Query(value = "lat") lat: Double, @Query(value = "lng") lng: Double, @Query(value = "distance") distance: Double): Call<List<ClinicAddress>>

    @GET("clinic/{id}")
    fun findClinic(@Path(value = "id") id: Int): Call<BaseResponse<Clinic>>
}