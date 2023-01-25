package br.com.petsus.normal.api.service.animal

import br.com.petsus.api.model.animal.Animal
import retrofit2.Response
import retrofit2.http.GET

interface AnimalRepositoryRetrofit {
    @GET("/")
    suspend fun list(): Response<List<Animal>>
}