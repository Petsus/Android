package br.com.petsus.api.model.animal

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import br.com.petsus.util.tool.format
import br.com.petsus.util.tool.isName
import br.com.petsus.util.tool.toDate
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Animal(
    @SerializedName("id") val id: Long,
    @SerializedName("name") var name: String,
    @SerializedName("photo") val photo: String?,
    @SerializedName("weight") var weight: Float,
    @SerializedName("height") var height: Int,
    @SerializedName("race") var race: Race,
    @SerializedName("birthday") var birthday: String,
    @SerializedName("qrcode") var qrcode: String?,
) : Parcelable, BaseObservable() {

    val formattedBirthday: String
        get() = birthday

    val age: Int
        get() {
            val date = birthday.toDate() ?: return 0
            val time = Date().time / 1000 - date.time / 1000
            return (time / 31536000).toInt()
        }

    @get:Bindable
    var currentQrCode: String?
        get() = qrcode
        set(value) {
            qrcode = value
            notifyPropertyChanged(BR.currentQrCode)
        }

    fun validate(): Boolean {
        return name.isName &&
                weight.run { this in 0f .. 1000f } &&
                height.run { this in 1..299 } &&
                birthday.toDate(pattern = "dd/MM/yyyy")?.before(Date().run { Date(time + (1000 * 60 * 60 * 24)) }) ?: true
    }

}
