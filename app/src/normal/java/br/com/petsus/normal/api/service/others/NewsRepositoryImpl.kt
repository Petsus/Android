package br.com.petsus.normal.api.service.others

import br.com.petsus.api.model.others.News
import br.com.petsus.api.service.others.NewsRepository
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.manager.ApiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepositoryImpl : NewsRepository {
    override fun news(): Flow<List<News>> {
        return flow {
            val news = ApiManager
                .create(NewRepositoryRetrofit::class.java)
                .all()
            send(news)
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class NewRepository {
    @Provides
    fun news(): NewsRepository = NewsRepositoryImpl()
}