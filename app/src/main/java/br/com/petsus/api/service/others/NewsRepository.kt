package br.com.petsus.api.service.others

import br.com.petsus.api.model.others.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun news(): Flow<List<News>>
}