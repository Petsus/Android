package br.com.petsus.api.service.auth

import androidx.annotation.WorkerThread
import br.com.petsus.api.model.auth.AuthToken
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val hasToken: Boolean

    var token: AuthToken?

    @WorkerThread
    fun fetchToken(): Flow<Result<AuthToken>?>
}