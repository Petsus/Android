package br.com.petsus.normal.api.service.user

import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.ChangePassword
import br.com.petsus.api.model.auth.ResetPassword
import br.com.petsus.api.model.base.EmptyResponse
import br.com.petsus.api.model.user.CreateUser
import br.com.petsus.api.model.user.User
import br.com.petsus.api.service.user.UserRepository
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.manager.ApiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl : UserRepository {
    override suspend fun resetPassword(body: ResetPassword): Flow<EmptyResponse> {
        return flow {
            val resetPassword = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .resetPassword(body = body)
            send(response = resetPassword)
        }
    }

    override suspend fun changePassword(body: ChangePassword): Flow<EmptyResponse> {
        return flow {
            val changePassword = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .changePassword(body = body)
            send(response = changePassword)
        }
    }

    override suspend fun createUser(body: CreateUser): Flow<AuthToken> {
        return flow {
            val createUser = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .createUser(body = body)
            send(response = createUser)
        }
    }

    override suspend fun getUser(): Flow<User> {
        return flow {
            val user = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .getUser()
            send(response = user)
        }
    }

    override suspend fun update(body: User): Flow<EmptyResponse> {
        return flow {
            val update = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .update(body = body)
            send(response = update)
        }
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class UserRepositoryModule {
    @Provides
    fun user(): UserRepository = UserRepositoryImpl()
}