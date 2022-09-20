package br.com.petsus.api.service

import br.com.petsus.api.model.base.BaseResponse
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.http.GET

interface ClinicRepository {
    @GET("")
    fun allClinic(): Call<BaseResponse<List<Any>>>
}