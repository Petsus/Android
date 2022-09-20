package br.com.petsus.api.model.auth

import com.google.gson.annotations.SerializedName

data class RefreshToken(
    @SerializedName("token") val token: String
)
