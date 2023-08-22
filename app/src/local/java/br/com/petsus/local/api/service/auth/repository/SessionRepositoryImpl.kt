package br.com.petsus.local.api.service.auth.repository

import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.service.auth.SessionRepository
import br.com.petsus.local.util.delayDefault
import com.bumptech.glide.load.model.Headers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SessionRepositoryImpl : SessionRepository {
    private var cachedToken: AuthToken? = null

    override val hasToken: Boolean
        get() = cachedToken != null

    override var token: AuthToken?
        get() = cachedToken
        set(value) { cachedToken = value }

    override fun headersGlide(): Headers = Headers { mutableMapOf() }

    override fun fetchToken(): Flow<Result<AuthToken>?> {
        return flow {
            delayDefault()

            val currentToken = cachedToken ?: run {
                emit(null)
                return@flow
            }

            if (currentToken.expiration >= System.currentTimeMillis()) {
                emit(Result.success(currentToken))
                return@flow
            }

            emit(Result.failure(InvalidRefreshToken(currentToken)))
        }
    }

    private class InvalidRefreshToken(authToken: AuthToken) : Throwable("Invalid authToken: $authToken")
}