package br.com.petsus.api.model.clinic

import com.google.gson.annotations.SerializedName

data class ClinicAddress(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("distance") val distance: Double,
)