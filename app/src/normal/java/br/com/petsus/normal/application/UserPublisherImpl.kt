package br.com.petsus.normal.application

import br.com.petsus.api.model.user.User
import br.com.petsus.application.preferences.AppPreferences
import br.com.petsus.service.publisher.UserPublisher
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.global.ResultState
import br.com.petsus.util.tool.Keys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class UserPublisherImpl(
    private val appPreferences: AppPreferences,
    private val coroutineScope: CoroutineScope,
) : UserPublisher {
    private val userFlow: MutableStateFlow<ResultState<User>> = MutableStateFlow(ResultState.Init())

    init {
        coroutineScope.launch {
            val user = getCurrentUser()
            if (user != null)
                userFlow.emit(ResultState.Success(data = user))
            else
                userFlow.emit(ResultState.Fail(error = StringFormatter()))
        }
    }

    override val current: Flow<User>
        get() = userFlow
            .map { result ->
                return@map when(result) {
                    is ResultState.Success -> result.data
                    is ResultState.Fail, is ResultState.Init -> getOrThrow()
                }
            }

    override suspend fun update(newUser: User) {
        appPreferences.putObject(Keys.KEY_USER.valueKey, newUser)
        userFlow.emit(ResultState.Success(newUser))
    }

    private fun getOrThrow(): User {
        return getCurrentUser() ?: throw Throwable("User not found")
    }

    private fun getCurrentUser(): User? {
        return appPreferences.getObject(Keys.KEY_USER.valueKey, User::class.java)
    }
}