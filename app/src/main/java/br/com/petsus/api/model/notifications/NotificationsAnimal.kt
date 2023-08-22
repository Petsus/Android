package br.com.petsus.api.model.notifications

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class NotificationsAnimal(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String?,
    @SerializedName("address") val address: String,
    @SerializedName("notificationID") val notificationID: String
) {
    val latLng: LatLng
        get() = LatLng(lat, lng)
}