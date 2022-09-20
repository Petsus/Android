package br.com.petsus.screen.home.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import br.com.petsus.api.service.APIRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData
import com.google.android.gms.maps.model.LatLng

class ClinicViewModel(
    private val repository: APIRepository
) : ViewModelLiveData() {

    private var lastLocation: Pair<LatLng, List<Any>>? = null

    fun load(location: LatLng): LiveData<List<Any>> {
        val last = lastLocation
        return when {
            last != null && last.equals(location) -> liveData { emit(last.second) }
            else -> repository.clinic().allClinic().toLiveDataBaseResponse()
        }
    }

}