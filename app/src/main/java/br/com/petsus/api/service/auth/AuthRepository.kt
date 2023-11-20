package br.com.petsus.api.service.auth

import br.com.petsus.api.model.auth.AuthLogin
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.RefreshToken
import br.com.petsus.api.model.base.BaseResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(body: AuthLogin): Flow<AuthToken>
    fun refreshToken(body: RefreshToken): Flow<AuthToken>
}