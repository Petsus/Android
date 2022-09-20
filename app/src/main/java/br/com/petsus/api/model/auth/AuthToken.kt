package br.com.petsus.api.model.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthToken(
    @SerializedName("token") val token: String,
    @SerializedName("dateExpiration") val expiration: Long,
    @SerializedName("type") val type: String
) : Parcelable {

    val completeToken: String
        get() = "$type $token"

}