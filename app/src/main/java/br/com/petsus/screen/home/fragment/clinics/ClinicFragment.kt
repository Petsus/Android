package br.com.petsus.screen.home.fragment.clinics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import br.com.petsus.api.model.clinic.ClinicAddress
import br.com.petsus.databinding.FragmentClinicBinding
import br.com.petsus.screen.home.fragment.clinics.details.ClinicDetailsBottomSheet
import br.com.petsus.util.base.fragment.AppFragment
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.tool.cast
import br.com.petsus.util.tool.configure
import br.com.petsus.util.tool.latLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClinicFragment : AppFragment<FragmentClinicBinding>() {

    private val viewModel: ClinicViewModel by appViewModels()

    private val requestLocation = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        viewModel.handlerPermission(result)
    }

    private val requestEnableGPS = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        viewModel.handlerEnableGPS(result)
    }

    private val maps: SupportMapFragment?
        get() {
            val id = binding?.containerClinic?.id ?: return null
            return childFragmentManager
                .findFragmentById(id)
                ?.cast<SupportMapFragment>()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.verifyPermissionGPS(requestLocation) { isEnable ->
            if (isEnable)
                viewModel.enableGPSAndLoadLocation(requestEnableGPS)
        }

        viewModel.location().observe(this) { currentLocation ->
            val supportMapFragment = maps ?: return@observe
            supportMapFragment.getMapAsync { googleMap ->
                googleMap.addMarker(
                    MarkerOptions()
                        .position(currentLocation.latLng)
                        .title("You")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation.latLng, 15f))
            }
        }

        viewModel.clinics().observe(this) { clinics ->
            val supportMapFragment = maps ?: return@observe
            supportMapFragment.getMapAsync { googleMap ->
                googleMap.configure()
                googleMap.addListener(clinics)

                clinics.forEach { clinic ->
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(clinic.lat, clinic.lng))
                            .title(clinic.name)
                    )
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentClinicBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun GoogleMap.addListener(clinics: List<ClinicAddress>) {
        setOnMarkerClickListener { marker ->
            val clinic = clinics.firstOrNull { clinic -> clinic.lat == marker.position.latitude && clinic.lng == marker.position.longitude } ?: return@setOnMarkerClickListener true
            ClinicDetailsBottomSheet(clinic.id)
                .show(childFragmentManager, null)
            return@setOnMarkerClickListener true
        }
    }

}