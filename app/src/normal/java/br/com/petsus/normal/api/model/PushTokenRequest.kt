package br.com.petsus.normal.api.model

import com.google.gson.annotations.SerializedName

data class PushTokenRequest(
    @SerializedName("token") val token: String
)
