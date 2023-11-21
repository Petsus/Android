package br.com.petsus.normal.api.service.history

import br.com.petsus.api.model.animal.CreateHistory
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.api.model.clinic.Exam
import br.com.petsus.api.model.clinic.Vaccine
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryMedicalRepositoryRetrofit {
    @GET("exam")
    suspend fun exams(@Query("q") query: String?): Response<BaseResponse<List<Exam>>>

    @GET("vaccine")
    suspend fun vaccines(@Query("q") query: String?): Response<BaseResponse<List<Vaccine>>>

    @GET("history")
    suspend fun all(): Response<BaseResponse<List<HistoryMedical>>>

    @POST("history")
    suspend fun create(@Body body: CreateHistory): Response<BaseResponse<HistoryMedical>>

    @DELETE("history/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}