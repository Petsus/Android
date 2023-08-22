package br.com.petsus.screen.splash

import android.app.Application
import androidx.lifecycle.liveData
import br.com.petsus.api.service.auth.SessionRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    application: Application,
    private val sessionRepository: SessionRepository
) : AppViewModel(application) {

    fun isLogged() = liveData {
        sessionRepository.fetchToken()
            .collector(this, viewModel = this@SplashViewModel)
    }

}