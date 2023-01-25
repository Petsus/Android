package br.com.petsus.local

import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.service.AnimalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class AnimalRepositoryImpl : AnimalRepository {
    override suspend fun all(): Flow<List<Animal>> {
        return flow {
            delay(4000)
            emit(listOf(Animal(Random.nextInt()), Animal(Random.nextInt()), Animal(Random.nextInt()), Animal(Random.nextInt()), Animal(Random.nextInt()), Animal(Random.nextInt()), Animal(Random.nextInt()), Animal(Random.nextInt())))
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object ApiModule {
    @Provides
    fun animal(): AnimalRepository = AnimalRepositoryImpl()
}