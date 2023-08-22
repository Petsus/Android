package br.com.petsus.screen.notification

import android.app.Application
import androidx.lifecycle.liveData
import br.com.petsus.api.service.notification.NotificationRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(application: Application) : AppViewModel(application) {
    @Inject
    lateinit var repository: NotificationRepository

    fun list() = liveData {
        repository.notification()
            .collector(this, viewModel = this@NotificationViewModel)
    }
}