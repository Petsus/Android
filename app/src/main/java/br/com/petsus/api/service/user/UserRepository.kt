package br.com.petsus.api.service.user

import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.ChangePassword
import br.com.petsus.api.model.auth.ResetPassword
import br.com.petsus.api.model.base.EmptyResponse
import br.com.petsus.api.model.user.CreateUser
import br.com.petsus.api.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun resetPassword(body: ResetPassword): Flow<EmptyResponse>
    suspend fun changePassword(body: ChangePassword): Flow<EmptyResponse>
    suspend fun createUser(body: CreateUser): Flow<AuthToken>
    suspend fun getUser(): Flow<User>
    suspend fun update(body: User): Flow<EmptyResponse>
    suspend fun name(): Flow<String>
}