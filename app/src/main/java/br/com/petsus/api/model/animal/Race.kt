package br.com.petsus.api.model.animal

import com.google.gson.annotations.SerializedName

data class Race(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("specie") val specie: Specie
)
