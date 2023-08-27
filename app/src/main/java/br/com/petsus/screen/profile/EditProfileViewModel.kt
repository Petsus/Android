package br.com.petsus.screen.profile

import android.app.Application
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.petsus.api.model.user.User
import br.com.petsus.api.service.user.UserRepository
import br.com.petsus.service.publisher.UserPublisher
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.base.viewmodel.MessageThrowable
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.global.Action
import br.com.petsus.util.tool.collector
import br.com.petsus.util.tool.isEmail
import br.com.petsus.util.tool.isName
import br.com.petsus.util.tool.isPhone
import br.com.petsus.util.tool.tryCatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    application: Application,
    private val publisher: UserPublisher,
    private val uiModel: EditProfileUiModel,
    private val userRepository: UserRepository
) : AppViewModel(application) {

    private val sharedFlow = userRepository.getUser()
        .shareIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1
        )

    val current: User?
        get() = sharedFlow.replayCache.firstOrNull()

    fun get() = liveData {
        sharedFlow.collector(this, viewModel = this@EditProfileViewModel)
    }

    fun update(user: User, loading: Action<Unit>) = liveData {
        tryCatch(
            viewModel = this@EditProfileViewModel,
            block = { user.validate() },
            completion = {
                loading.action(Unit)
                userRepository.update(body = user)
                    .collector(
                        liveDataScope = this,
                        viewModel = this@EditProfileViewModel,
                        onCollect = { publisher.update(user) }
                    )
            }
        )
    }

    private fun User.validate() {
        this.name.isName.validate(uiModel.invalidName)
        this.email.isEmail.validate(uiModel.invalidEmail)
        this.phone.isPhone.or(this.phone.isNullOrEmpty()).validate(uiModel.invalidPhone)
    }

    private fun Boolean.validate(message: StringFormatter) {
        if (!this)
            throw MessageThrowable(message)
    }

}