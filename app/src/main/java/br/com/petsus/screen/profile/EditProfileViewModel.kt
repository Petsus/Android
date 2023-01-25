package br.com.petsus.screen.profile

import androidx.lifecycle.liveData
import br.com.petsus.api.model.user.User
import br.com.petsus.api.service.user.UserRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor() : ViewModelLiveData() {

    @Inject lateinit var userRepository: UserRepository

    fun get() = liveData {
        userRepository.getUser()
            .collector(this)
    }

    fun update(user: User) = liveData {
        userRepository.update(body = user)
            .collector(this)
    }

}