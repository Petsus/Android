package br.com.petsus.api.service.animal

import br.com.petsus.api.model.animal.CreateHistory
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.api.model.clinic.Exam
import br.com.petsus.api.model.clinic.Vaccine
import kotlinx.coroutines.flow.Flow

interface HistoryMedicalRepository {
    fun history(): Flow<List<HistoryMedical>>
    fun exams(search: String): Flow<List<Exam>>
    fun vaccines(search: String): Flow<List<Vaccine>>
    fun delete(historyMedical: HistoryMedical): Flow<Boolean>
    fun create(historyMedical: CreateHistory): Flow<HistoryMedical>
}