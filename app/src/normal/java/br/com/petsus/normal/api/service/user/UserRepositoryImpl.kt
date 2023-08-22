package br.com.petsus.normal.api.service.user

import android.content.Context
import android.net.Uri
import br.com.petsus.BuildConfig
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.ChangePassword
import br.com.petsus.api.model.auth.ResetPassword
import br.com.petsus.api.model.base.EmptyResponse
import br.com.petsus.api.model.user.CreateUser
import br.com.petsus.api.model.user.User
import br.com.petsus.api.service.user.UserRepository
import br.com.petsus.application.preferences.AppPreferences
import br.com.petsus.normal.api.exception.send
import br.com.petsus.normal.api.exception.sendBoolean
import br.com.petsus.normal.api.manager.ApiManager
import br.com.petsus.normal.util.toMultipart
import br.com.petsus.util.tool.Keys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val context: Context,
    private val appPreferences: AppPreferences
) : UserRepository {
    override fun resetPassword(body: ResetPassword): Flow<EmptyResponse> {
        return flow {
            val resetPassword = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .resetPassword(body = body)
            send(response = resetPassword)
        }
    }
    override fun changePassword(body: ChangePassword): Flow<EmptyResponse> {
        return flow {
            val changePassword = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .changePassword(body = body)
            send(response = changePassword)
        }
    }
    override fun createUser(body: CreateUser): Flow<AuthToken> {
        return flow {
            val createUser = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .createUser(body = body)
            send(response = createUser)
        }
    }
    override fun getUser(): Flow<User> {
        return flow {
            val user = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .getUser()
            send(response = user)
        }
    }
    override fun update(body: User): Flow<EmptyResponse> {
        return flow {
            val update = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .update(body = body)
            send(response = update)
        }
    }
    override fun name(): Flow<String> {
        return flow {
            emit(appPreferences.getObject(Keys.KEY_USERNAME.valueKey, String::class.java) ?: "")
        }
    }
    override fun saveImage(uri: Uri): Flow<Boolean> {
        return flow {
            val update = ApiManager
                .create(UserRepositoryRetrofit::class.java)
                .saveImage(file = uri.toMultipart(context))
            sendBoolean(update)
        }
    }
    override fun currentImage(): Flow<String?> {
        return flow {
            emit(BuildConfig.BASE_URL + "user/image")
        }
    }

}

@Module
@InstallIn(ViewModelComponent::class)
class UserRepositoryModule {
    @Provides
    fun user(@ApplicationContext context: Context, preferences: AppPreferences): UserRepository =
        UserRepositoryImpl(appPreferences = preferences, context = context)
}