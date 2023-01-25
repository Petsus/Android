package br.com.petsus.api.model.address

import com.google.gson.annotations.SerializedName

data class State(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("initials") val initials: String,
    @SerializedName("ibge_id") val ibgeId: Int,
)