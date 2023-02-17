package br.com.petsus.api.model.clinic

import br.com.petsus.api.model.address.Address
import com.google.gson.annotations.SerializedName

data class Clinic(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("address") val address: Address,
    @SerializedName("image") val image: String?,
)
