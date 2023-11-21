package br.com.petsus.screen.animal.history.add.dialog.exam

import android.app.Application
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.petsus.api.model.clinic.Vaccine
import br.com.petsus.api.service.animal.HistoryMedicalRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.baseFlow
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVaccineHistoryViewModel @Inject constructor(application: Application) : AppViewModel(application) {

    @Inject
    lateinit var historyRepository: HistoryMedicalRepository

    private var searchJob: Job? = null
    private val searchVaccine: MutableStateFlow<List<Pair<String, Vaccine>>> = MutableStateFlow(emptyList())

    fun observer() = liveData {
        searchVaccine.collector(this, viewModel = this@SearchVaccineHistoryViewModel)
    }

    fun search(text: String?) {
        searchJob?.cancel()
        if (text == null || text.length < 3)
            return
        searchJob = viewModelScope.launch(viewModelScope.coroutineContext) {
            delay(3000)

            historyRepository.vaccines(text)
                .baseFlow(viewModel = this@SearchVaccineHistoryViewModel)
                .collect { items ->
                    searchVaccine.emit(items.map { item -> item.name to item })
                }
        }
    }
}