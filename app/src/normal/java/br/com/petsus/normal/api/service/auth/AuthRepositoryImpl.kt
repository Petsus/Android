package br.com.petsus.normal.api.service.auth

import br.com.petsus.api.model.auth.AuthLogin
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.RefreshToken
import br.com.petsus.api.service.auth.AuthRepository
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.manager.ApiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(body: AuthLogin): Flow<AuthToken> {
        return flow {
            val login = ApiManager
                .create(AuthRepositoryRetrofit::class.java)
                .login(body = body)
            send(response = login)
        }
    }

    override suspend fun refreshToken(body: RefreshToken): Flow<AuthToken> {
        return flow {
            val refreshToken = ApiManager
                .create(AuthRepositoryRetrofit::class.java)
                .refreshToken(body = body)
            send(response = refreshToken)
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class AuthRepositoryModule {
    @Provides
    fun auth(): AuthRepository = AuthRepositoryImpl()
}