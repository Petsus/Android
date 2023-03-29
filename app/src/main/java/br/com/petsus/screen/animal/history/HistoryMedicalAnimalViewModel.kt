package br.com.petsus.screen.animal.history

import androidx.lifecycle.liveData
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryMedicalAnimalViewModel @Inject constructor() : ViewModelLiveData() {

    @Inject
    lateinit var repository: AnimalRepository

    fun history() = liveData {
        repository.historyMedical()
            .collector(this)
    }

}