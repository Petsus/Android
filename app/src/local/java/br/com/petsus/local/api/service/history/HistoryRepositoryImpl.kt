package br.com.petsus.local.api.service.history

import br.com.petsus.api.model.animal.CreateHistory
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.api.model.clinic.Exam
import br.com.petsus.api.model.clinic.Vaccine
import br.com.petsus.api.service.animal.HistoryMedicalRepository
import br.com.petsus.local.util.delayDefault
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class HistoryRepositoryImpl : HistoryMedicalRepository {
    private fun generateExam() = Exam(
        id = Random.nextLong(Long.MAX_VALUE),
        name = "Exam ${Random.nextLong(Long.MAX_VALUE)}"
    )

    private fun generateHistoryMedical(): HistoryMedical {
        return HistoryMedical(
            id = Random.nextLong(Long.MAX_VALUE),
            createdAt = "2023-02-13 12:00:00",
            exam = null,
            clinic = "",
            veterinary = "",
            vaccine = Vaccine(
                id = Random.nextLong(Long.MAX_VALUE),
                name = "Vaccine name"
            )
        )
    }

    private fun generateVaccines() = Vaccine(
        id = Random.nextLong(Long.MAX_VALUE),
        name = "Vaccine ${Random.nextLong(Long.MAX_VALUE)}"
    )

    override fun history(): Flow<List<HistoryMedical>> {
        return flow {
            delayDefault()
            emit(
                listOf(generateHistoryMedical(), generateHistoryMedical(), generateHistoryMedical(), generateHistoryMedical(), generateHistoryMedical(), generateHistoryMedical())
            )
        }
    }

    override fun exams(search: String): Flow<List<Exam>> {
        return flow {
            delayDefault()
            emit(
                listOf(generateExam(), generateExam(), generateExam())
            )
        }
    }

    override fun vaccines(search: String): Flow<List<Vaccine>> {
        return flow {
            delayDefault()
            emit(
                listOf(generateVaccines(), generateVaccines(), generateVaccines())
            )
        }
    }

    override fun delete(historyMedical: HistoryMedical): Flow<Boolean> {
        return flow {
            delayDefault()
            emit(value = true)
        }
    }

    override fun create(historyMedical: CreateHistory): Flow<HistoryMedical> {
        return flow {
            delayDefault()
            emit(generateHistoryMedical())
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class HistoryModule {
    @Provides
    fun history(): HistoryMedicalRepository = HistoryRepositoryImpl()
}