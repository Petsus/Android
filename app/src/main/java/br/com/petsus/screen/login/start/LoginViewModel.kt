package br.com.petsus.screen.login.start

import android.app.Application
import androidx.lifecycle.liveData
import br.com.petsus.api.model.auth.AuthLogin
import br.com.petsus.api.model.auth.AuthToken
import br.com.petsus.api.model.auth.ChangePassword
import br.com.petsus.api.model.auth.ResetPassword
import br.com.petsus.api.model.user.CreateUser
import br.com.petsus.api.service.auth.AuthRepository
import br.com.petsus.api.service.auth.SessionRepository
import br.com.petsus.api.service.user.UserRepository
import br.com.petsus.application.preferences.AppPreferences
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.Keys
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(application: Application) : AppViewModel(application) {

    @Inject
    lateinit var authRepository: AuthRepository
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var sharedPreferences: AppPreferences
    @Inject
    lateinit var sessionRepository: SessionRepository

    fun login(
        email: String?,
        password: String?,
    ) = liveData {
        authRepository.login(AuthLogin(email = email, password = password))
            .map { token ->
                loadUser(token)
                return@map token
            }
            .collector(this, viewModel = this@LoginViewModel)
    }

    fun resetPassword(
        email: String?
    ) = liveData {
        userRepository.resetPassword(ResetPassword(email = email))
            .collector(this, viewModel = this@LoginViewModel)
    }

    fun updatePassword(
        email: String?,
        password: String?,
        token: String?
    ) = liveData {
        userRepository.changePassword(ChangePassword(email = email, password = password, token = token))
            .collector(this, viewModel = this@LoginViewModel)
    }

    fun createUser(
        name: String?,
        email: String?,
        password: String?,
        phone: String?
    ) = liveData {
        userRepository.createUser(CreateUser(name = name, email = email, password = password, phone = phone))
            .map { token ->
                loadUser(token)
                return@map token
            }
            .collector(this, onCollect = { authToken ->
                sharedPreferences.putObject(Keys.KEY_TOKEN.valueKey, authToken)
            }, viewModel = this@LoginViewModel)
    }

    private suspend fun loadUser(token: AuthToken) {
        sessionRepository.token = token
        userRepository.getUser()
            .collector(viewModel = this, onCollect =  { user ->
                sharedPreferences.putObject(Keys.KEY_USER.valueKey, user)
                sharedPreferences.putObject(Keys.KEY_USERNAME.valueKey, user.name)
            })
    }

}