package br.com.petsus.api.model.address

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("ibge_id") val ibgeId: Int,
    @SerializedName("state") val state: State
)
