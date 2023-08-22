package br.com.petsus.screen.home.fragment.scan.dialog

import android.app.Application
import androidx.lifecycle.liveData
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.util.base.viewmodel.LocationViewModel
import br.com.petsus.util.tool.collector
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanAnimalViewModel @Inject constructor(
    application: Application
) : LocationViewModel(application) {
    @Inject lateinit var repository: AnimalRepository

    fun getAnimal(animalTagId: String) = liveData {
        repository.getAnimalForTagId(animalTagId)
            .collector(this, viewModel = this@ScanAnimalViewModel)
    }
    fun sendAnimalFound(animal: Animal, location: LatLng) = liveData {
        repository.notifyAnimalFounded(animal, location)
            .collector(this, viewModel = this@ScanAnimalViewModel)
    }
}