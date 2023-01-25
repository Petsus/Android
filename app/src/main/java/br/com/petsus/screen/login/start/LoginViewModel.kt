package br.com.petsus.screen.login.start

import androidx.lifecycle.liveData
import br.com.petsus.api.model.auth.AuthLogin
import br.com.petsus.api.model.auth.ChangePassword
import br.com.petsus.api.model.auth.ResetPassword
import br.com.petsus.api.model.user.CreateUser
import br.com.petsus.api.service.auth.AuthRepository
import br.com.petsus.api.service.user.UserRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModelLiveData() {

    @Inject lateinit var authRepository: AuthRepository
    @Inject lateinit var userRepository: UserRepository

    fun login(
        email: String?,
        password: String?,
        googleAuthCode: String?
    ) = liveData {
        authRepository.login(AuthLogin(email = email, password = password, googleAuthCode = googleAuthCode))
            .collector(this)
    }

    fun resetPassword(
        email: String?
    ) = liveData {
        userRepository.resetPassword(ResetPassword(email = email))
            .collector(this)
    }

    fun updatePassword(
        email: String?,
        password: String?,
        token: String?
    ) = liveData {
        userRepository.changePassword(ChangePassword(email = email, password = password, token = token))
            .collector(this)
    }

    fun createUser(
        name: String?,
        email: String?,
        password: String?,
        phone: String?
    ) = liveData {
        userRepository.createUser(CreateUser(name = name, email = email, password = password, phone = phone))
            .collector(this)
    }

}