package br.com.petsus.api.model.animal

import com.google.gson.annotations.SerializedName

data class Animal(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("photo") val photo: String?,
    @SerializedName("race") val race: Race,
)
