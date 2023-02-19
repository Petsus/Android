package br.com.petsus.api.model.clinic

import com.google.gson.annotations.SerializedName

data class Exam(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)
