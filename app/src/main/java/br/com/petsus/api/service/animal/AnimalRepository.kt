package br.com.petsus.api.service.animal

import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.model.animal.CreateAnimal
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.api.model.animal.Race
import br.com.petsus.api.model.animal.Specie
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AnimalRepository {
    fun all(): Flow<List<Animal>>
    fun species(): Flow<List<Specie>>
    fun races(animal: Animal): Flow<List<Race>>
    fun races(specie: Specie): Flow<List<Race>>
    fun registerQrCode(animal: Animal): Flow<String>
    fun unregisterQrCode(qrCode: String): Flow<Unit>
    fun updateImage(animalId: Long, fileImage: File): Flow<Boolean>
    fun updateAnimal(animal: Animal): Flow<Boolean>
    fun deleteAnimal(animal: Animal): Flow<Boolean>
    fun getAnimalForTagId(tagId: String): Flow<Animal>
    fun notifyAnimalFounded(animal: Animal, location: LatLng): Flow<Unit>
    fun createAnimal(animal: CreateAnimal): Flow<Animal>
}