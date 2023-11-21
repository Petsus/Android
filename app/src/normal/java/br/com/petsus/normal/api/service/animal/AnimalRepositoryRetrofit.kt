package br.com.petsus.normal.api.service.animal

import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.model.animal.CreateAnimal
import br.com.petsus.api.model.animal.Race
import br.com.petsus.api.model.animal.Specie
import br.com.petsus.api.model.base.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimalRepositoryRetrofit {
    @GET("animal")
    suspend fun list(): Response<BaseResponse<List<Animal>>>
    @GET("races")
    suspend fun races(): Response<BaseResponse<List<Race>>>
    @GET("races/specie/{specieId}")
    suspend fun races(@Path(value = "specieId") specieId: Long): Response<BaseResponse<List<Race>>>
    @GET("species")
    suspend fun species(): Response<BaseResponse<List<Specie>>>
    @GET("animal/qrcode/{animalId}")
    suspend fun registerQrCode(@Path(value = "animalId") animalId: Long): Response<BaseResponse<String>>
    @DELETE("animal/qrcode/{animalId}")
    suspend fun unregisterQrCode(@Path(value = "animalId") animalId: String): Response<Unit>
    @DELETE("animal/{animalId}")
    suspend fun delete(@Path(value = "animalId") animalId: Long): Response<Unit>
    @GET("animal/tag/{tagId}")
    suspend fun getAnimalForTag(@Path(value = "tagId") tag: String): Response<BaseResponse<Animal>>
    @POST("animal")
    suspend fun createAnimal(@Body animal: CreateAnimal): Response<BaseResponse<Animal>>
    @PUT("animal/{animalId}")
    suspend fun updateAnimal(@Path(value = "animalId") animalId: Long, @Body animal: Animal): Response<BaseResponse<Animal>>
    @PUT("animal/image/{id}")
    suspend fun updateImage(@Path(value = "id") id: Long, @Part body: MultipartBody.Part): Response<Unit>
    @POST("animal/found")
    suspend fun notifyAnimalFounded(@Query("lat") lat: Double, @Query("lng") lng: Double, @Query("animalId") animalId: Long): Response<Unit>
}

