package br.com.petsus.local.api.service.animal

import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.local.util.delayDefault
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnimalRepositoryImpl : AnimalRepository {
    override suspend fun all(): Flow<List<Animal>> {
        return flow {
            delayDefault()
            emit(emptyList())
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class AnimalRepositoryModule {
    @Provides
    fun animal(): AnimalRepository = AnimalRepositoryImpl()
}