package br.com.petsus.api.model.clinic

import br.com.petsus.api.model.address.Address
import br.com.petsus.api.model.animal.Specie
import com.google.gson.annotations.SerializedName

data class Clinic(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("address") val address: Address,
    @SerializedName("image") val image: String?,
    @SerializedName("species") val species: List<Specie>,
    @SerializedName("exams") val exams: List<Exam>
) {

    val maskPhone: String?
        get() = phone?.run { "(${this.substring(0, 2)}) ${this.substring(2)}" }

}
