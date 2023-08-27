package br.com.petsus.local.api.service.user.repository

import android.net.Uri
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
    override fun resetPassword(body: ResetPassword): Flow<EmptyResponse> {
        return flow {
            delayDefault()
            emit(EmptyResponse())
        }
    }

    override fun changePassword(body: ChangePassword): Flow<EmptyResponse> {
        return flow {
            delayDefault()
            emit(EmptyResponse())
        }
    }

    override fun createUser(body: CreateUser): Flow<AuthToken> {
        return flow {
            delayDefault()
            emit(AuthRepositoryImpl().generateToken())
        }
    }

    override fun getUser(): Flow<User> {
        return flow {
            delayDefault()
            emit(
                User(
                    id = 1,
                    name = "User name test",
                    email = "email@mail.com.br",
                    enable = true,
                    createdAt = "2023-01-01 00:00:00",
                    updatedAt = "2023-01-01 00:00:01",
                    phone = "15987654321",
                    emailVerified = "2023-01-01 00:00:02",
                    phoneVerified = "2023-01-01 00:00:03"
                )
            )
        }
    }

    override fun update(body: User): Flow<EmptyResponse> {
        return flow {
            delayDefault()
            emit(EmptyResponse())
        }
    }

    override fun saveImage(uri: Uri): Flow<Boolean> {
        return flow {
            delayDefault()
            emit(true)
        }
    }

    override fun currentImage(): Flow<String> {
        return flow {
            delayDefault()
            emit("https://steamuserimages-a.akamaihd.net/ugc/2029481402824246918/420C7ED8187DB71DAB443C734FD4BD9E984DA14D/?imw=637&imh=358&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=true")
        }
    }
}