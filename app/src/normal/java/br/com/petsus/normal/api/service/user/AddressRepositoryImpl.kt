package br.com.petsus.normal.api.service.user

import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.api.model.address.CreateOrUpdateAddress
import br.com.petsus.api.service.user.AddressRepository
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.exception.sendBoolean
import br.com.petsus.normal.api.manager.ApiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddressRepositoryImpl : AddressRepository {
    override fun list(): Flow<List<AppAddress>> {
        return flow {
            val response = ApiManager
                .create(AddressRepositoryRetrofit::class.java)
                .list()
            send(response = response)
        }
    }

    override fun delete(appAddress: AppAddress): Flow<Boolean> {
        return flow {
            val response = ApiManager
                .create(AddressRepositoryRetrofit::class.java)
                .delete(id = appAddress.id)
            sendBoolean(response)
        }
    }

    override fun save(appAddress: CreateOrUpdateAddress): Flow<AppAddress> {
        return flow {
            val response = ApiManager
                .create(AddressRepositoryRetrofit::class.java)
                .run {
                    if (appAddress.id == null) create(appAddress) else update(appAddress)
                }
            send(response = response)
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class AddressModule {
    @Provides
    fun address(): AddressRepository = AddressRepositoryImpl()
}