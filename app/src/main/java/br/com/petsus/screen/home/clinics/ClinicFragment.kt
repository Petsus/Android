package br.com.petsus.screen.home.clinics

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import br.com.petsus.databinding.FragmentClinicBinding
import br.com.petsus.util.base.fragment.BaseFragment
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.tool.cast
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ClinicFragment : BaseFragment<FragmentClinicBinding>() {

    private val viewModel: ClinicViewModel by appViewModels()

    private val maps: SupportMapFragment?
        get() {
            val id = binding?.containerClinic?.id ?: return null
            return childFragmentManager
                .findFragmentById(id)
                ?.cast<SupportMapFragment>()
        }

    private val requestLocation = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isAuthorized ->
        if (isAuthorized)
            load()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentClinicBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            load()
        else
            requestLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun load() {
        maps?.let { supportMapFragment ->
            supportMapFragment.getMapAsync { googleMap ->
                googleMap.configure()
                val context = context ?: return@getMapAsync
                val request = LocationRequest.create()
                    .setMaxWaitTime(1000)
                    .setSmallestDisplacement(0f)
                    .setInterval(0)

                val settingRequest = LocationSettingsRequest.Builder()
                    .addLocationRequest(request)
                    .build()

                val providerClient = LocationServices.getFusedLocationProviderClient(context)

                LocationServices.getSettingsClient(context)
                    .checkLocationSettings(settingRequest)
                    .addOnCompleteListener {
                        providerClient.getCurrentLocation(
                            CurrentLocationRequest.Builder().build(), null
                        ).addOnSuccessListener { currentLocation ->
                            viewModel.load(LatLng(currentLocation.latitude, currentLocation.longitude)).observe(viewLifecycleOwner) { clinics ->
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(currentLocation.latitude, currentLocation.longitude))
                                        .title("You")
                                )
                                clinics.forEach { clinic ->
                                    googleMap.addMarker(
                                        MarkerOptions()
                                            .position(LatLng(clinic.lat, clinic.lng))
                                            .title(clinic.name)
                                    )
                                }

                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude), 15f))
                            }
                        }
                    }
            }
        }
    }

    private fun GoogleMap.configure() {
        uiSettings.isCompassEnabled = false
        uiSettings.isMapToolbarEnabled = false

        setOnMarkerClickListener {
            ClinicDetailsBottomSheet(1)
                .show(childFragmentManager, null)
            return@setOnMarkerClickListener true
        }

        setOnCameraIdleListener {

        }
    }

}