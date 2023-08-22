package br.com.petsus.api.service.auth

import androidx.annotation.WorkerThread
import br.com.petsus.api.model.auth.AuthToken
import com.bumptech.glide.load.model.Headers
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val hasToken: Boolean
    var token: AuthToken?
    fun headersGlide(): Headers
    @WorkerThread
    fun fetchToken(): Flow<Result<AuthToken>?>
}