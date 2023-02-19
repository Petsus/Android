package br.com.petsus.local.api.service.animal

import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.model.animal.Race
import br.com.petsus.api.model.animal.Specie
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.local.util.delayDefault
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class AnimalRepositoryImpl : AnimalRepository {
    private fun generateAnimal(): Animal {
        return Animal(
            id = Random.nextLong(Long.MAX_VALUE),
            name = "Animal name",
            photo = "https://s2.glbimg.com/cYa3pKAKIPidjKyGPuAd8T4Hd1I=/e.glbimg.com/og/ed/f/original/2017/08/21/dog-2570398_960_720.jpg",
            race = Race(
                id = Random.nextLong(Long.MAX_VALUE),
                name = "Race animal",
                specie = Specie(
                    id = Random.nextLong(Long.MAX_VALUE),
                    name = "Specie animal"
                )
            )
        )
    }
    override suspend fun all(): Flow<List<Animal>> {
        return flow {
            delayDefault()
            emit(
                listOf(
                    generateAnimal(), generateAnimal(), generateAnimal()
                )
            )
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class AnimalRepositoryModule {
    @Provides
    fun animal(): AnimalRepository = AnimalRepositoryImpl()
}