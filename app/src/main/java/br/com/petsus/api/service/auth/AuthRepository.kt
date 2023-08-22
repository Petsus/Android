package br.com.petsus.api.service.auth

import br.com.petsus.api.model.auth.AuthLogin
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.RefreshToken
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(body: AuthLogin): Flow<AuthToken>
    fun refreshToken(body: RefreshToken): Flow<AuthToken>
}