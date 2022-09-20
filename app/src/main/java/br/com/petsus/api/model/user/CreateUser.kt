package br.com.petsus.api.model.user

import com.google.gson.annotations.SerializedName

data class CreateUser(
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)
