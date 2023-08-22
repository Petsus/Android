package br.com.petsus.normal.api.service.animal

import android.content.Context
import androidx.core.net.toUri
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.model.animal.CreateAnimal
import br.com.petsus.api.model.animal.Race
import br.com.petsus.api.model.animal.Specie
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.normal.api.exception.ApiException
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.manager.ApiManager
import br.com.petsus.normal.util.toMultipart
import com.google.android.gms.maps.model.LatLng
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class AnimalRepositoryImpl(
    private val context: Context
) : AnimalRepository {
    override fun all(): Flow<List<Animal>> {
        return flow {
            val list = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .list()
            send(response = list)
        }
    }
    override fun species(): Flow<List<Specie>> {
        return flow {
            val list = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .species()
            send(response = list)
        }
    }
    override fun races(animal: Animal): Flow<List<Race>> {
        return flow {
            val races = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .races()
            send(response = races)
        }
    }
    override fun races(specie: Specie): Flow<List<Race>> {
        return flow {
            val races = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .races(specieId = specie.id)
            send(response = races)
        }
    }
    override fun registerQrCode(animal: Animal): Flow<String> {
        return flow {
            val response = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .registerQrCode(animal.id)
            send(response = response)
        }
    }
    override fun unregisterQrCode(qrCode: String): Flow<Unit> {
        return flow {
            val response = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .unregisterQrCode(qrCode)
            send(response = response)
        }
    }
    override fun updateImage(animalId: Long, fileImage: File): Flow<Boolean> {
        return flow {
            val response = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .updateImage(animalId, fileImage.toUri().toMultipart(context))
            if (response.isSuccessful) {
                emit(response.isSuccessful)
                return@flow
            }
            throw ApiException(response)
        }
    }
    override fun updateAnimal(animal: Animal): Flow<Boolean> {
        return flow {
            val response = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .updateAnimal(animalId = animal.id, animal)
            if (response.isSuccessful) {
                emit(response.isSuccessful)
                return@flow
            }
            throw ApiException(response)
        }
    }
    override fun deleteAnimal(animal: Animal): Flow<Boolean> {
        return flow {
            val response = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .delete(animal.id)
            emit(response.isSuccessful)
        }
    }
    override fun getAnimalForTagId(tagId: String): Flow<Animal> {
        return flow {
            val response = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .getAnimalForTag(tagId)
            send(response = response)
        }
    }
    override fun notifyAnimalFounded(animal: Animal, location: LatLng): Flow<Unit> {
        return flow {
            val response = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .notifyAnimalFounded(lat = location.latitude, lng = location.longitude, animal.id)
            if (response.isSuccessful) {
                emit(Unit)
                return@flow
            }
            throw ApiException(response)
        }
    }
    override fun createAnimal(animal: CreateAnimal): Flow<Animal> {
        return flow {
            val response = ApiManager
                .create(AnimalRepositoryRetrofit::class.java)
                .createAnimal(animal)
            send(response = response)
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class AnimalRepositoryModule {
    @Provides
    fun animal(@ApplicationContext context: Context): AnimalRepository = AnimalRepositoryImpl(context)
}