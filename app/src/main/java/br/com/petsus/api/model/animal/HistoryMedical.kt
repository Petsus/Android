package br.com.petsus.api.model.animal

import br.com.petsus.api.model.clinic.Exam
import br.com.petsus.api.model.clinic.Vaccine
import com.google.gson.annotations.SerializedName

data class HistoryMedical(
    @SerializedName("id") val id: Long,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("exams") val exam: Exam?,
    @SerializedName("vaccine") val vaccine: Vaccine?
) {

    val name: String?
        get() = exam?.name ?: vaccine?.name

}