package br.com.petsus.api.service.clinic

import br.com.petsus.api.model.clinic.Clinic
import br.com.petsus.api.model.clinic.ClinicAddress
import kotlinx.coroutines.flow.Flow

interface ClinicRepository {
    suspend fun find(id: Long): Flow<Clinic>
    suspend fun list(lat: Double, lng: Double, distance: Double): Flow<List<ClinicAddress>>
}