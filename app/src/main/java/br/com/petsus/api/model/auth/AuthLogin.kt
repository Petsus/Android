package br.com.petsus.api.model.auth

import com.google.gson.annotations.SerializedName

data class AuthLogin(
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?,
)
