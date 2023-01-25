package br.com.petsus.api.model.address

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("id") val id: Long,
    @SerializedName("address") val address: String,
    @SerializedName("number") val number: Int,
    @SerializedName("complement") val complement: String?,
    @SerializedName("neighborhood") val neighborhood: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("city") val city: City,
    @SerializedName("postal_code") val postalCode: String
) {

    val completeAddress: String
        get() = "$address - $number ${complement ?: ""}, $neighborhood, ${city.name}/${city.state.initials}"

}
