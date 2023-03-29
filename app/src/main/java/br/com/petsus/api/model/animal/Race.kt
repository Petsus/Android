package br.com.petsus.api.model.animal

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Race(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("specie") val specie: Specie
) : Parcelable
