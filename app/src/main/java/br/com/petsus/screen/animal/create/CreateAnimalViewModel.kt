package br.com.petsus.screen.animal.create

import android.app.Application
import androidx.lifecycle.liveData
import br.com.petsus.api.model.animal.CreateAnimal
import br.com.petsus.api.model.animal.Specie
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.api.service.user.AddressRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.collector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAnimalViewModel @Inject constructor(application: Application) : AppViewModel(application) {

    @Inject
    lateinit var repository: AnimalRepository

    @Inject
    lateinit var addressesRepository: AddressRepository

    fun species() = liveData {
        repository.species()
            .collector(this, viewModel = this@CreateAnimalViewModel)
    }

    fun races(specie: Specie) = liveData {
        repository.races(specie = specie)
            .collector(this, viewModel = this@CreateAnimalViewModel)
    }

    fun addresses() = liveData {
        addressesRepository.list()
            .collector(this, viewModel = this@CreateAnimalViewModel)
    }

    fun create(
        animal: CreateAnimal
    ) = liveData {
        repository.createAnimal(animal)
            .collector(this, viewModel = this@CreateAnimalViewModel)
    }
}