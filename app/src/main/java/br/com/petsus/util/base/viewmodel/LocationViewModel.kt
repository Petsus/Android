package br.com.petsus.util.base.viewmodel

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.petsus.util.global.Action
import br.com.petsus.util.tool.latLng
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

abstract class LocationViewModel(application: Application) : AppViewModel(application) {
    private var callbackPermissionGPS: Action<Boolean>? = null
    private val currentLocation: MutableLiveData<Location> = MutableLiveData()

    protected open fun load(latLng: LatLng) = Unit

    fun location(): LiveData<Location> =
        this.currentLocation

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