package br.com.petsus.screen.home.fragment.clinics

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import br.com.petsus.api.model.clinic.ClinicAddress
import br.com.petsus.api.service.clinic.ClinicRepository
import br.com.petsus.util.global.Action
import br.com.petsus.util.tool.collector
import br.com.petsus.util.tool.latLng
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClinicViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    @Inject
    lateinit var clinicRepository: ClinicRepository

    private var callbackPermissionGPS: Action<Boolean>? = null

    private val clinics: MutableLiveData<List<ClinicAddress>> = MutableLiveData()
    private val currentLocation: MutableLiveData<Location> = MutableLiveData()

    private fun load(
        location: LatLng
    ) {
        viewModelScope.launch {
            clinicRepository.list(lat = location.latitude, lng = location.longitude, distance = 5000.0)
                .collect { items -> clinics.postValue(items) }
        }
    }

    fun clinics(): LiveData<List<ClinicAddress>> = this.clinics

    fun location(): LiveData<Location> = this.currentLocation

    fun find(
        id: Long
    ) = liveData {
        clinicRepository.find(id)
            .collector(this)
    }

    fun verifyPermissionGPS(launcher: ActivityResultLauncher<String>, callback: Action<Boolean>) {
        when (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            PackageManager.PERMISSION_GRANTED -> callback.action(true)
            else -> {
                this.callbackPermissionGPS = callback
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    fun handlerPermission(result: Boolean) {
        this.callbackPermissionGPS?.action(result)
    }

    fun enableGPSAndLoadLocation(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        LocationServices.getSettingsClient(getApplication<Application>())
            .checkLocationSettings(
                LocationSettingsRequest.Builder()
                    .addLocationRequest(LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1L).build())
                    .build()
            ).addOnSuccessListener {
                startObserver()
            }.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    runCatching {
                        launcher.launch(IntentSenderRequest.Builder(exception.resolution).build())
                    }
                }
            }
    }

    fun handlerEnableGPS(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK)
            startObserver()
    }

    @SuppressLint("MissingPermission")
    private fun startObserver() {
        LocationServices.getFusedLocationProviderClient(getApplication<Application>())
            .getCurrentLocation(CurrentLocationRequest.Builder().build(), null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    load(location.latLng)
                    currentLocation.postValue(location)
                }
            }
    }

}