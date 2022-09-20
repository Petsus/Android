package br.com.petsus.api.model.user

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String?,
    @SerializedName("enable") val enable: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("emailVerified") val emailVerified: String?,
    @SerializedName("phoneVerified") val phoneVerified: String?,
)
