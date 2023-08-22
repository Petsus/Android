package br.com.petsus.api.model.address

import com.google.gson.annotations.SerializedName

data class CreateOrUpdateAddress(
    @SerializedName("id") val id: Long?,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("number") val number: Int,
    @SerializedName("address") val address: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("complement") val complement: String?,
    @SerializedName("postalCode") val postalCode: String?,
    @SerializedName("neighborhood") val neighborhood: String,
)