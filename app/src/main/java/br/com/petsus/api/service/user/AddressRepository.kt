package br.com.petsus.api.service.user

import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.api.model.address.CreateOrUpdateAddress
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun list(): Flow<List<AppAddress>>
    fun delete(appAddress: AppAddress): Flow<Boolean>
    fun save(appAddress: CreateOrUpdateAddress): Flow<AppAddress>
}