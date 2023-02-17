package br.com.petsus.local.api.service.auth

import br.com.petsus.api.model.auth.AuthLogin
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.RefreshToken
import br.com.petsus.api.service.auth.AuthRepository
import br.com.petsus.local.util.delayDefault
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl : AuthRepository {

    fun generateToken() = AuthToken(
        token = "authToken",
        expiration = System.currentTimeMillis() + (1000 * 60 * 90),
        type = "Bearer"
    )

    override suspend fun login(body: AuthLogin): Flow<AuthToken> {
        return flow {
            delayDefault()
            emit(generateToken())
        }
    }

    override suspend fun refreshToken(body: RefreshToken): Flow<AuthToken> {
        return flow {
            delayDefault()
            emit(generateToken())
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class AuthRepositoryModule {
    @Provides
    fun auth(): AuthRepository = AuthRepositoryImpl()
}