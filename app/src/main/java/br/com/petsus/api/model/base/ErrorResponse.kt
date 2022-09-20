package br.com.petsus.api.model.base

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") var data: Any
)