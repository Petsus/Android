package br.com.petsus.api.service.auth

import androidx.annotation.WorkerThread
import br.com.petsus.api.model.auth.AuthToken

interface SessionRepository {
    val hasToken: Boolean

    var token: AuthToken?

    @WorkerThread
    fun fetchToken(): AuthToken?
}