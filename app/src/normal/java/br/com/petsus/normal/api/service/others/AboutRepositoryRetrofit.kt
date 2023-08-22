package br.com.petsus.normal.api.service.others

import retrofit2.Call
import retrofit2.http.GET

interface AboutRepositoryRetrofit {
    @GET("about")
    fun about(): Call<String>
}