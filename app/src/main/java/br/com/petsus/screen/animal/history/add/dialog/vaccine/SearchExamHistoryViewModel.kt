package br.com.petsus.screen.animal.history.add.dialog.vaccine

import android.app.Application
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.petsus.api.model.clinic.Exam
import br.com.petsus.api.service.animal.HistoryMedicalRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchExamHistoryViewModel @Inject constructor(application: Application) : AppViewModel(application) {

    @Inject
    lateinit var historyRepository: HistoryMedicalRepository

    private var searchJob: Job? = null
    private val searchVaccine: MutableStateFlow<List<Pair<String, Exam>>> = MutableStateFlow(emptyList())

    fun observer() = liveData {
        searchVaccine.collector(this, viewModel = this@SearchExamHistoryViewModel)
    }

    fun search(text: String?) {
        searchJob?.cancel()
        if (text == null || text.length < 3)
            return
        searchJob = viewModelScope.launch(viewModelScope.coroutineContext) {
            delay(3000)

            historyRepository.exams(text)
                .collect { items ->
                    searchVaccine.emit(items.map { item -> item.name to item })
                }
        }
    }
}