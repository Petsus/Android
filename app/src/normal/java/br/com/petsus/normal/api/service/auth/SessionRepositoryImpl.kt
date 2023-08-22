package br.com.petsus.normal.api.service.auth

import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.service.auth.SessionRepository
import br.com.petsus.normal.api.manager.SessionManager
import com.bumptech.glide.load.model.Headers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SessionRepositoryImpl : SessionRepository {
    override val hasToken: Boolean
        get() = SessionManager.current.hasToken

    override var token: AuthToken?
        get() = SessionManager.current.token
        set(value) { SessionManager.current.token = value }
    override fun headersGlide(): Headers {
        return Headers {
            return@Headers mutableMapOf(
                Pair("Authorization", SessionManager.current.token?.completeToken ?: "")
            )
        }
    }
    override fun fetchToken(): Flow<Result<AuthToken>?> = flow {
        emit(SessionManager.current.fetchToken())
    }
}