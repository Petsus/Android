package br.com.petsus.normal.api.service.auth

import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.service.auth.SessionRepository
import br.com.petsus.normal.api.manager.SessionManager

class SessionRepositoryImpl : SessionRepository {
    override val hasToken: Boolean
        get() = SessionManager.current.hasToken

    override var token: AuthToken?
        get() = SessionManager.current.token
        set(value) { SessionManager.current.token = value }

    override fun fetchToken(): AuthToken? = SessionManager.current.fetchToken()
}