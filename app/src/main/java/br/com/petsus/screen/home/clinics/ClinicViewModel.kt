package br.com.petsus.screen.home.clinics

import androidx.lifecycle.liveData
import br.com.petsus.api.service.clinic.ClinicRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData
import br.com.petsus.util.tool.collector
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClinicViewModel @Inject constructor() : ViewModelLiveData() {

    @Inject lateinit var clinicRepository: ClinicRepository

    fun load(
        location: LatLng
    ) = liveData {
        clinicRepository.list(lat = location.latitude, lng = location.longitude, distance = 5000.0)
            .collector(this)
    }

    fun find(
        id: Long
    ) = liveData {
        clinicRepository.find(id)
            .collector(this)
    }
}