package br.com.petsus.api.model.address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class State(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("initials") val initials: String,
    @SerializedName("ibge_id") val ibgeId: Int,
) : Parcelable