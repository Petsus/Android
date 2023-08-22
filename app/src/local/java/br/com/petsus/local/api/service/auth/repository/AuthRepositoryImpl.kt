package br.com.petsus.local.api.service.auth.repository

import br.com.petsus.api.model.auth.AuthLogin
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.RefreshToken
import br.com.petsus.api.service.auth.AuthRepository
import br.com.petsus.local.util.delayDefault
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl : AuthRepository {

    fun generateToken() = AuthToken(
        token = "authToken",
        expiration = System.currentTimeMillis() + (1000 * 60 * 90),
        type = "Bearer"
    )

    override fun login(body: AuthLogin): Flow<AuthToken> {
        return flow {
            delayDefault()
            emit(generateToken())
        }
    }

    override fun refreshToken(body: RefreshToken): Flow<AuthToken> {
        return flow {
            delayDefault()
            emit(generateToken())
        }
    }
}