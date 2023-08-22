package br.com.petsus.screen.animal.history

import android.app.Application
import androidx.lifecycle.liveData
import br.com.petsus.api.model.animal.CreateHistory
import br.com.petsus.api.model.animal.HistoryMedical
import br.com.petsus.api.service.animal.HistoryMedicalRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryMedicalAnimalViewModel @Inject constructor(application: Application) : AppViewModel(application) {

    @Inject
    lateinit var repository: HistoryMedicalRepository


    fun history() = liveData {
        repository.history()
            .collector(this, viewModel = this@HistoryMedicalAnimalViewModel)
    }
    fun delete(historyMedical: HistoryMedical) = liveData {
        repository.delete(historyMedical)
            .collector(this, viewModel = this@HistoryMedicalAnimalViewModel)
    }

    fun create(history: CreateHistory)  = liveData {
        repository.create(history)
            .collector(this, viewModel = this@HistoryMedicalAnimalViewModel)
    }
}