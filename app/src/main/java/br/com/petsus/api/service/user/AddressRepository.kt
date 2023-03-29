package br.com.petsus.api.service.user

import br.com.petsus.api.model.address.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    suspend fun list(): Flow<List<Address>>
    suspend fun delete(address: Address): Flow<Boolean>
}