package br.com.petsus.normal.api.service.animal

import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.manager.ApiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnimalRepositoryImpl : AnimalRepository {
    override suspend fun all(): Flow<List<Animal>> {
        return flow {
            val list = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .list()
            send(response = list)
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class AnimalRepositoryModule {
    @Provides
    fun animal(): AnimalRepository = AnimalRepositoryImpl()
}