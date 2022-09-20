package br.com.petsus.screen.login.start

import br.com.petsus.api.model.auth.AuthLogin
import br.com.petsus.api.model.auth.ChangePassword
import br.com.petsus.api.model.auth.ResetPassword
import br.com.petsus.api.model.user.CreateUser
import br.com.petsus.api.service.APIRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData

class LoginViewModel(
    private val repository: APIRepository
) : ViewModelLiveData() {

    fun login(email: String?, password: String?, googleAuthCode: String?) =
        repository.auth().login(AuthLogin(email = email, password = password, googleAuthCode = googleAuthCode)).toLiveData()

    fun resetPassword(email: String?) =
        repository.user().resetPassword(ResetPassword(email = email)).toLiveData()

    fun updatePassword(email: String?, password: String?, token: String?) =
        repository.user().changePassword(ChangePassword(email = email, password = password, token = token)).toLiveData()

    fun createUser(name: String?, email: String?, password: String?) =
        repository.user().createUser(CreateUser(name = name, email = email, password = password)).toLiveData()

}