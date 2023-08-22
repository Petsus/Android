package br.com.petsus.normal.api.service.others

import br.com.petsus.api.service.others.AboutAppRepository
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.manager.ApiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse

class AboutRepositoryImpl : AboutAppRepository {
    override fun about(): Flow<String> {
        return flow {
            val response = ApiManager
                .create(AboutRepositoryRetrofit::class.java)
                .about()
            send(response = response.awaitResponse())
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class AboutModule {
    @Provides
    fun about(): AboutAppRepository = AboutRepositoryImpl()
}