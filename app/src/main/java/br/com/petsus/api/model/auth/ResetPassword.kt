package br.com.petsus.api.model.auth

import com.google.gson.annotations.SerializedName

data class ResetPassword(
    @SerializedName("email") val email: String?
)