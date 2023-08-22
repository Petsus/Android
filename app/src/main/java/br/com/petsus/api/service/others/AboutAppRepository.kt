package br.com.petsus.api.service.others

import kotlinx.coroutines.flow.Flow

interface AboutAppRepository {
    fun about(): Flow<String>
}