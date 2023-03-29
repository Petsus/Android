package br.com.petsus.local.api.service.others.repository

import br.com.petsus.api.service.others.AboutAppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AboutAppRepositoryImpl : AboutAppRepository {
    override suspend fun about(): Flow<String> {
        return flow {
            emit("<h1>About app html</h1>")
        }
    }
}