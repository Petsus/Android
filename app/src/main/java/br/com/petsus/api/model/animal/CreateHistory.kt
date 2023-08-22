package br.com.petsus.api.model.animal

import br.com.petsus.api.model.clinic.Exam
import br.com.petsus.api.model.clinic.Vaccine
import br.com.petsus.util.base.viewmodel.MessageThrowable
import br.com.petsus.util.tool.messageString
import com.google.gson.annotations.SerializedName

data class CreateHistory(
    @SerializedName("examsId") val exam: Long?,
    @SerializedName("vaccineId") val vaccine: Long?,
    @SerializedName("veterinary") val veterinary: String,
    @SerializedName("animalId") val animalId: Long,
) {
    class Builder {
        private var exam: Exam? = null
        private var vaccine: Vaccine? = null
        private var veterinary: String? = null
        private var animalId: Long? = null

        fun setExam(value: Exam): Builder {
            this.exam = value
            return this
        }

        fun setVaccine(value: Vaccine): Builder {
            this.vaccine = value
            return this
        }

        fun setVeterinary(value: String): Builder {
            this.veterinary = value
            return this
        }

        fun setAnimalId(value: Long): Builder {
            this.animalId = value
            return this
        }

        @Throws(MessageThrowable::class)
        fun build(): CreateHistory {
            synchronized(this) {
                runCatching {
                    assert(exam == null && vaccine == null) { "Select one exam or vaccine" }
                    assert(veterinary == null) { "Write one veterinary" }
                    assert(animalId == null) { "Animal is null" }
                }.onFailure { error ->
                    throw MessageThrowable(error.messageString)
                }

                return CreateHistory(
                    exam = exam?.id,
                    vaccine = vaccine?.id,
                    veterinary = veterinary!!,
                    animalId = animalId!!
                )
            }
        }
    }
}
