package br.com.petsus.local.api.service.user.repository

import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.api.model.address.City
import br.com.petsus.api.model.address.CreateOrUpdateAddress
import br.com.petsus.api.model.address.State
import br.com.petsus.api.service.user.AddressRepository
import br.com.petsus.local.util.delayDefault
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class AddressRepositoryImpl : AddressRepository {
    private fun generateAddress(): AppAddress {
        return AppAddress(
            id = 1,
            address = "R. Teodoro Kaisel",
            number = 883,
            complement = null,
            neighborhood = "Vila Hortência",
            lat = -23.5053214,
            lng = -47.4373164,
            City(
                id = 1,
                name = "Sorocaba",
                ibgeId = 1,
                state = State(
                    id = 1,
                    name = "São Paulo",
                    initials = "SP",
                    ibgeId = 1
                )
            ),
            postalCode = "18020-268",
            nickname = "Nickname",
        )
    }

    override fun list(): Flow<List<AppAddress>> {
        return flow {
            emit(listOf(generateAddress(), generateAddress(), generateAddress()))
        }
    }

    override fun delete(appAddress: AppAddress): Flow<Boolean> {
        return flow {
            delayDefault()
            emit(Random.nextBoolean())
        }
    }

    override fun save(appAddress: CreateOrUpdateAddress): Flow<AppAddress> {
        return flow {
            delayDefault()
            emit(generateAddress())
        }
    }
}