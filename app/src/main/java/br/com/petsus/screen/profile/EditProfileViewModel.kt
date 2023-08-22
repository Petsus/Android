package br.com.petsus.screen.profile

import android.app.Application
import androidx.lifecycle.liveData
import br.com.petsus.api.model.user.User
import br.com.petsus.api.service.user.UserRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(application: Application) : AppViewModel(application) {

    @Inject lateinit var userRepository: UserRepository

    fun get() = liveData {
        userRepository.getUser()
            .collector(this, viewModel = this@EditProfileViewModel)
    }

    fun update(user: User) = liveData {
        userRepository.update(body = user)
            .collector(this, viewModel = this@EditProfileViewModel)
    }

}