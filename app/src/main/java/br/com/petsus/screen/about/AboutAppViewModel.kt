package br.com.petsus.screen.about

import android.app.Application
import androidx.lifecycle.liveData
import br.com.petsus.api.service.others.AboutAppRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutAppViewModel @Inject constructor(application: Application) : AppViewModel(application) {

    @Inject
    lateinit var aboutAppRepository: AboutAppRepository

    fun about() = liveData {
        aboutAppRepository.about()
            .collector(this, viewModel = this@AboutAppViewModel)
    }

}