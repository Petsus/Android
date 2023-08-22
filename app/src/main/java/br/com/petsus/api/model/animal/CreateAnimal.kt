package br.com.petsus.api.model.animal

import android.net.Uri
import br.com.petsus.util.base.viewmodel.MessageThrowable
import br.com.petsus.util.tool.isName
import br.com.petsus.util.tool.messageString
import com.google.gson.annotations.SerializedName

data class CreateAnimal(
    val photo: Uri?,
    @SerializedName("name") var name: String,
    @SerializedName("weight") var weight: Float,
    @SerializedName("height") var height: Int,
    @SerializedName("raceId") var raceId: Long,
    @SerializedName("birthday") var birthday: String,
    @SerializedName("addressId") var addressId: Long,
) {
    class Builder {
        private var name: String? = null
        private var photo: Uri? = null
        private var weight: Float? = null
        private var height: Int? = null
        private var raceId: Long? = null
        private var birthday: String? = null
        private var addressId: Long? = null

        fun setName(value: String): Builder {
            this.name = value
            return this
        }

        fun setPhoto(value: Uri?): Builder {
            this.photo = value
            return this
        }

        fun setWeight(value: Float): Builder {
            this.weight = value
            return this
        }

        fun setHeight(value: Int): Builder {
            this.height = value
            return this
        }

        fun setRaceId(value: Long): Builder {
            this.raceId = value
            return this
        }

        fun setBirthday(value: String): Builder {
            this.birthday = value
            return this
        }

        fun setAddressId(value: Long): Builder {
            this.addressId = value
            return this
        }

        @Throws(MessageThrowable::class)
        fun build(): CreateAnimal {
            synchronized(this) {
                runCatching {
                    assert(name != null && name.isName) { "Name invalid" }
                    assert(weight != null) { "Weight invalid" }
                    assert(height != null) { "Height invalid" }
                    assert(raceId != null) { "RaceId invalid" }
                    assert(birthday != null) { "Birthday invalid" }
                    assert(addressId != null) { "AddressId Invalid" }
                }.onFailure { error ->
                    throw MessageThrowable(error.messageString)
                }

                return CreateAnimal(
                    name = name!!,
                    photo = photo,
                    weight = weight!!,
                    height = height!!,
                    raceId = raceId!!,
                    birthday = birthday!!,
                    addressId = addressId!!
                )
            }
        }
    }
}
