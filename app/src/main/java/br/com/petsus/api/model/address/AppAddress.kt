package br.com.petsus.api.model.address

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppAddress(
    @SerializedName("id") val id: Long,
    @SerializedName("address") val address: String,
    @SerializedName("number") val number: Int,
    @SerializedName("complement") val complement: String?,
    @SerializedName("neighborhood") val neighborhood: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("city") val city: City,
    @SerializedName("postalCode") val postalCode: String?,
    @SerializedName("nickname") val nickname: String?
) : Parcelable {

    val completeAddress: String
        get() = "$address - $number ${complement ?: ""}, $neighborhood, ${city.name}/${city.state.initials}"

    val location: LatLng
        get() = LatLng(lat, lng)

}
