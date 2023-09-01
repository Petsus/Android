package br.com.petsus.service.publisher

import br.com.petsus.api.model.address.AppAddress
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface AddressPublisher {
    val current: Flow<List<AppAddress>>
    suspend fun update(items: List<AppAddress>)
}