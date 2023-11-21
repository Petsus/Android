package br.com.petsus.normal.api.service.notification

import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.api.model.base.EmptyResponse
import br.com.petsus.api.model.notifications.Notifications
import br.com.petsus.api.model.notifications.NotificationsAnimal
import br.com.petsus.normal.api.model.PushTokenRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface NotificationRepositoryRetrofit {
    @GET("notification")
    suspend fun list(): Response<BaseResponse<List<Notifications>>>
    @PUT("notification/push-token")
    fun registerPushToken(@Body pushTokenRequest: PushTokenRequest): Response<EmptyResponse>
    @DELETE("notification/push-token")
    fun unregisterPushToken(@Header("Authorization") token: String, @Body pushTokenRequest: PushTokenRequest): Response<EmptyResponse>
    @GET("notification/{id}")
    suspend fun details(@Path(value = "id") id: String): Response<BaseResponse<NotificationsAnimal>>
    @GET
    suspend fun image(@Url urlImage: String): Response<ResponseBody>
}