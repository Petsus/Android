package br.com.petsus.api.model.clinic

import com.google.gson.annotations.SerializedName

data class Vaccine(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
)
