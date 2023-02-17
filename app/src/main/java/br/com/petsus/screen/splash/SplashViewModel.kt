package br.com.petsus.screen.splash

import androidx.lifecycle.liveData
import br.com.petsus.api.service.auth.SessionRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModelLiveData() {

    @Inject lateinit var sessionRepository: SessionRepository

    fun isLogged() = liveData {
        sessionRepository.fetchToken()
            .collector(this)
    }

}