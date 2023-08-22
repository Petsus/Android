package br.com.petsus.local.api.service.animal

import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.model.animal.CreateAnimal
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.api.model.animal.Race
import br.com.petsus.api.model.animal.Specie
import br.com.petsus.api.model.clinic.Vaccine
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.local.util.delayDefault
import com.google.android.gms.maps.model.LatLng
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import kotlin.random.Random

class AnimalRepositoryImpl : AnimalRepository {
    private fun generateRace(): Race {
        return Race(
            id = Random.nextLong(Long.MAX_VALUE),
            name = "Race animal",
            specie = Specie(
                id = Random.nextLong(Long.MAX_VALUE),
                name = "Specie animal"
            )
        )
    }
    private fun generateAnimal(): Animal {
        return Animal(
            id = Random.nextLong(Long.MAX_VALUE),
            name = "Animal name",
            photo = "https://s2.glbimg.com/cYa3pKAKIPidjKyGPuAd8T4Hd1I=/e.glbimg.com/og/ed/f/original/2017/08/21/dog-2570398_960_720.jpg",
            weight = 45.50f,
            height = 120,
            birthday = "13/02/2022",
            race = generateRace(),
            addressId = 1,
            qrcode = "f1875fd1e76afdf663dafcd064ae51345bcc4aa8fcd5f73b1c3614383e7931c1"
        )
    }
    override fun all(): Flow<List<Animal>> {
        return flow {
            delayDefault()
            emit(
                listOf(
                    generateAnimal(), generateAnimal(), generateAnimal()
                )
            )
        }
    }

    override fun species(): Flow<List<Specie>> {
        return flow {
            TODO("Not yet implemented")
        }
    }

    override fun races(animal: Animal): Flow<List<Race>> {
        return flow {
            delayDefault()
            emit(
                listOf(
                    generateRace(), generateRace(), generateRace(), generateRace(), generateRace(), generateRace()
                )
            )
        }
    }

    override fun races(specie: Specie): Flow<List<Race>> {
        return flow {
            TODO("Not yet implemented")
        }
    }

    override fun registerQrCode(animal: Animal): Flow<String> {
        return flow {
            delayDefault()
            emit("f1875fd1e76afdf663dafcd064ae51345bcc4aa8fcd5f73b1c3614383e7931c1")
        }
    }

    override fun unregisterQrCode(qrCode: String): Flow<Unit> {
        return flow {
            delayDefault()
            emit(Unit)
        }
    }

    override fun updateImage(animalId: Long, fileImage: File): Flow<Boolean> {
        return flow {
            delayDefault()
            emit(value = true)
        }
    }

    override fun updateAnimal(animal: Animal): Flow<Boolean> {
        return flow {
            delayDefault()
            emit(value = true)
        }
    }

    override fun deleteAnimal(animal: Animal): Flow<Boolean> {
        return flow {
            delayDefault()
            emit(value = true)
        }
    }
    override fun getAnimalForTagId(tagId: String): Flow<Animal> {
        return flow {
            delayDefault()
            emit(generateAnimal())
        }
    }

    override fun notifyAnimalFounded(animal: Animal, location: LatLng): Flow<Unit> {
        return flow {
            delayDefault()
            emit(Unit)
        }
    }

    override fun createAnimal(animal: CreateAnimal): Flow<Animal> {
        return flow {
            delayDefault()
            emit(generateAnimal())
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class AnimalRepositoryModule {
    @Provides
    fun animal(): AnimalRepository = AnimalRepositoryImpl()
}