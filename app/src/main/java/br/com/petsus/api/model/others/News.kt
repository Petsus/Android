package br.com.petsus.api.model.others

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("img") val img: String
)
