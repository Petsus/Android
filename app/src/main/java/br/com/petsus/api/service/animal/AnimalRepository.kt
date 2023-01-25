package br.com.petsus.api.service.animal

import br.com.petsus.api.model.animal.Animal
import kotlinx.coroutines.flow.Flow

interface AnimalRepository {
    suspend fun all(): Flow<List<Animal>>
}