package br.com.petsus.normal.api.service.history

import br.com.petsus.api.model.animal.CreateHistory
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.api.model.clinic.Exam
import br.com.petsus.api.model.clinic.Vaccine
import br.com.petsus.api.service.animal.HistoryMedicalRepository
import br.com.petsus.normal.api.exception.ApiException
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.manager.ApiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryMedicalRepositoryImpl : HistoryMedicalRepository {
    override fun history(): Flow<List<HistoryMedical>> {
        return flow {
            val request = ApiManager
                .create(HistoryMedicalRepositoryRetrofit::class.java)
                .all()
            send(response = request)
        }
    }

    override fun exams(search: String): Flow<List<Exam>> {
        return flow {
            val request = ApiManager
                .create(HistoryMedicalRepositoryRetrofit::class.java)
                .exams(search)
            send(response = request)
        }
    }

    override fun vaccines(search: String): Flow<List<Vaccine>> {
        return flow {
            val request = ApiManager
                .create(HistoryMedicalRepositoryRetrofit::class.java)
                .vaccines(search)
            send(response = request)
        }
    }

    override fun delete(historyMedical: HistoryMedical): Flow<Boolean> {
        return flow {
            val response = ApiManager
                .create(HistoryMedicalRepositoryRetrofit::class.java)
                .delete(historyMedical.id)
            if (response.isSuccessful) {
                emit(response.isSuccessful)
                return@flow
            }
            throw ApiException(response)
        }
    }

    override fun create(historyMedical: CreateHistory): Flow<HistoryMedical> {
        return flow {
            val request = ApiManager
                .create(HistoryMedicalRepositoryRetrofit::class.java)
                .create(historyMedical)
            send(response = request)
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class HistoryModule {
    @Provides
    fun history(): HistoryMedicalRepository = HistoryMedicalRepositoryImpl()
}