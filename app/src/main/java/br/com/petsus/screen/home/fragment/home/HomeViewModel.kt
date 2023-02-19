package br.com.petsus.screen.home.fragment.home

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.model.others.News
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.api.service.others.NewsRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData
import br.com.petsus.util.global.ResultState
import br.com.petsus.util.tool.collectorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModelLiveData() {

    @set:Inject
    var newsRepository: NewsRepository? = null
        set(value) {
            field = value
            viewModelScope.launch {
                value?.news()?.collectorState(stateFlow = flowStateNews)
            }
        }

    @set:Inject
    var animalRepository: AnimalRepository? = null
        set(value) {
            field = value
            viewModelScope.launch {
                value?.all()?.collectorState(stateFlow = flowStateAnimal)
            }
        }

    private val flowStateNews: MutableStateFlow<ResultState<List<News>>> = MutableStateFlow(ResultState.Init())

    private val flowStateAnimal: MutableStateFlow<ResultState<List<Animal>>> = MutableStateFlow(ResultState.Init())

    fun news() = flowStateNews.asLiveData()

    fun animals() = flowStateAnimal.asLiveData()

}