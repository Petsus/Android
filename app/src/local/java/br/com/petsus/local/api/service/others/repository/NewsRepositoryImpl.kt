package br.com.petsus.local.api.service.others.repository

import br.com.petsus.api.model.others.News
import br.com.petsus.api.service.others.NewsRepository
import br.com.petsus.local.util.delayDefault
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class NewsRepositoryImpl : NewsRepository {
    private fun generateNews() = News(
        id = Random.nextLong(Long.MAX_VALUE),
        name = "Name news",
        url = "https://g1.globo.com/planeta-bizarro/noticia/2015/06/veja-guaxinim-que-saltou-nas-costas-de-jacare-e-outros-animais-audaciosos.html",
        img = "https://extra.globo.com/incoming/16450873-7d0-996/w976h550-PROP/guaxinim-jacare.jpg",
    )
    override fun news(): Flow<List<News>> = flow {
        delayDefault()
        emit(
            listOf(generateNews(), generateNews(), generateNews(), generateNews())
        )
    }
}