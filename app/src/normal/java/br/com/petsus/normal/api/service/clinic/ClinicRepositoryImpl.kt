package br.com.petsus.normal.api.service.clinic

import br.com.petsus.api.model.clinic.Clinic
import br.com.petsus.api.model.clinic.ClinicAddress
import br.com.petsus.api.service.clinic.ClinicRepository
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.manager.ApiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClinicRepositoryImpl : ClinicRepository {
    override suspend fun find(id: Long): Flow<Clinic> {
        return flow {
            val find = ApiManager
                .create(ClinicRepositoryRetrofit::class.java)
                .find(id = id)
            send(response = find)
        }
    }

    override suspend fun list(lat: Double, lng: Double, distance: Double): Flow<List<ClinicAddress>> {
        return flow {
            val list = ApiManager
                .create(ClinicRepositoryRetrofit::class.java)
                .list(lat = lat, lng = lng, distance = distance)
            send(response = list)
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class ClinicRepositoryModule {
    @Provides
    fun clinic(): ClinicRepository = ClinicRepositoryImpl()
}