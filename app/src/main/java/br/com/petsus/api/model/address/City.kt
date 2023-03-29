package br.com.petsus.api.model.address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("ibge_id") val ibgeId: Int,
    @SerializedName("state") val state: State
) : Parcelable
