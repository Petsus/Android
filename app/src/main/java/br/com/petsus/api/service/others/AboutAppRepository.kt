package br.com.petsus.api.service.others

import kotlinx.coroutines.flow.Flow

interface AboutAppRepository {
    suspend fun about(): Flow<String>
}