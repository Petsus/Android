package br.com.petsus.screen.animal

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnimalViewModel @Inject constructor() : ViewModelLiveData() {

    @Inject lateinit var repository: AnimalRepository

    val animals: LiveData<List<Animal>> = liveData {
        repository.all().collector(this)
    }

}