package br.com.petsus.local.api.service.user.repository

import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.ChangePassword
import br.com.petsus.api.model.auth.ResetPassword
import br.com.petsus.api.model.base.EmptyResponse
import br.com.petsus.api.model.user.CreateUser
import br.com.petsus.api.model.user.User
import br.com.petsus.api.service.user.UserRepository
import br.com.petsus.local.api.service.auth.repository.AuthRepositoryImpl
import br.com.petsus.local.util.delayDefault
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl : UserRepository {
    override suspend fun resetPassword(body: ResetPassword): Flow<EmptyResponse> {
        return flow {
            delayDefault()
            emit(EmptyResponse())
        }
    }

    override suspend fun changePassword(body: ChangePassword): Flow<EmptyResponse> {
        return flow {
            delayDefault()
            emit(EmptyResponse())
        }
    }

    override suspend fun createUser(body: CreateUser): Flow<AuthToken> {
        return flow {
            delayDefault()
            emit(AuthRepositoryImpl().generateToken())
        }
    }

    override suspend fun getUser(): Flow<User> {
        return flow {
            delayDefault()
            emit(
                User(
                    id = 1,
                    name = "User name test",
                    email = "email@mail.com.br",
                    enable = true,
                    createdAt = "2023-01-01 00:00:00",
                    updatedAt = "2023-01-01 00:00:00",
                    phone = "15987654321",
                    emailVerified = "2023-01-01 00:00:00",
                    phoneVerified = "2023-01-01 00:00:00"
                )
            )
        }
    }

    override suspend fun update(body: User): Flow<EmptyResponse> {
        return flow {
            delayDefault()
            emit(EmptyResponse())
        }
    }

    override suspend fun name(): Flow<String> {
        return flow {
            getUser().collect {
                emit(it.name)
            }
        }
    }
}