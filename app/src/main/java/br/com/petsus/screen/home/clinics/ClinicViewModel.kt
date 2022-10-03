package br.com.petsus.screen.home.clinics

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import br.com.petsus.api.model.clinic.ClinicAddress
import br.com.petsus.api.service.APIRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData
import com.google.android.gms.maps.model.LatLng

class ClinicViewModel(
    private val repository: APIRepository
) : ViewModelLiveData() {

    private var lastLocation: Pair<LatLng, List<ClinicAddress>>? = null

    fun load(location: LatLng): LiveData<List<ClinicAddress>> {
        val last = lastLocation
        return when {
            last != null && last.equals(location) -> liveData { emit(last.second) }
            else -> repository.clinic().allClinic(lat = location.latitude, lng = location.longitude, distance = 5000.0).toLiveData()
        }
    }

    fun findClinic(id: Int) =
        repository.clinic().findClinic(id = id).toLiveData()

}