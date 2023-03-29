package br.com.petsus.api.service.animal

import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.api.model.animal.Race
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AnimalRepository {
    suspend fun all(): Flow<List<Animal>>
    suspend fun races(animal: Animal): Flow<List<Race>>

    suspend fun registerQrCode(): Flow<String>

    suspend fun unregisterQrCode(qrCode: String): Flow<Unit>

    suspend fun historyMedical(): Flow<List<HistoryMedical>>

    suspend fun updateImage(fileImage: File): Flow<Boolean>

    suspend fun updateAnimal(animal: Animal): Flow<Boolean>
}