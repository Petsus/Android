package br.com.petsus.screen.home.fragment.clinics

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.petsus.api.model.clinic.ClinicAddress
import br.com.petsus.api.service.clinic.ClinicRepository
import br.com.petsus.util.base.viewmodel.LocationViewModel
import br.com.petsus.util.tool.baseFlow
import br.com.petsus.util.tool.collector
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClinicViewModel @Inject constructor(
    application: Application
) : LocationViewModel(application) {

    @Inject
    lateinit var clinicRepository: ClinicRepository

    private val clinics: MutableLiveData<List<ClinicAddress>> = MutableLiveData()

    override fun load(
        latLng: LatLng
    ) {
        viewModelScope.launch {
            clinicRepository.list(lat = latLng.latitude, lng = latLng.longitude, distance = 5000.0)
                .baseFlow(this@ClinicViewModel)
                .collect { items -> clinics.postValue(items) }
        }
    }

    fun clinics(): LiveData<List<ClinicAddress>> = this.clinics

    fun find(
        id: Long
    ) = liveData {
        clinicRepository.find(id)
            .baseFlow(this@ClinicViewModel)
            .collector(this, viewModel = this@ClinicViewModel)
    }
}