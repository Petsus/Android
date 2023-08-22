package br.com.petsus.screen.address.fragment

import android.location.Address
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.databinding.FragmentAddressMapsBinding
import br.com.petsus.util.base.fragment.AppFragment
import br.com.petsus.util.base.fragment.findNavigation
import br.com.petsus.util.global.router.Navigator
import br.com.petsus.util.tool.completeAddress
import br.com.petsus.util.tool.configure
import br.com.petsus.util.tool.dp
import br.com.petsus.util.tool.location
import br.com.petsus.util.tool.parcelable
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class AddressMapsFragment : AppFragment<FragmentAddressMapsBinding>() {
    companion object {
        const val ADDRESS = "data_address"
        const val SEARCH_ADDRESS = "data_search_address"
    }

    private val supportMapsFragment = SupportMapFragment.newInstance(
        GoogleMapOptions()
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddressMapsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            Navigator.of(containerMapsAddress).present(supportMapsFragment)
            appbarAddressFragment.setOnBackClickListener { findNavigation()?.dismiss() }
        }

        loadValues()
    }

    private fun loadValues() {
        val bundleArguments = arguments ?: return run { findNavigation()?.dismiss() }
        when {
            bundleArguments.containsKey(ADDRESS) -> loadUpdateAddress(bundleArguments = bundleArguments)
            bundleArguments.containsKey(SEARCH_ADDRESS) -> loadSearchAddress(bundleArguments = bundleArguments)
            else -> run { findNavigation()?.dismiss() }
        }
    }

    private fun loadUpdateAddress(bundleArguments: Bundle) {
        val appAddress = bundleArguments.parcelable(ADDRESS, AppAddress::class.java) ?: return run { findNavigation()?.dismiss() }
        loadInfoMaps(title = appAddress.completeAddress, appAddress.location)
        continueFlow(AddressInfoFragment.ADDRESS, appAddress)

    }

    private fun loadSearchAddress(bundleArguments: Bundle) {
        val address = bundleArguments.parcelable(SEARCH_ADDRESS, Address::class.java) ?: return run { findNavigation()?.dismiss() }
        loadInfoMaps(title = address.completeAddress, location = address.location)
        continueFlow(AddressInfoFragment.SEARCH_ADDRESS, address)
    }

    private fun loadInfoMaps(title: String, location: LatLng) {
        binding?.appbarAddressFragment?.subtitle = title
        supportMapsFragment.getMapAsync { googleMaps ->
            googleMaps.configure()
            googleMaps.setOnCameraMoveListener { binding?.markMapsAddress?.translationY = (-32f).dp }
            googleMaps.setOnCameraIdleListener { binding?.markMapsAddress?.translationY = (-16f).dp }

            googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
        }
    }

    private fun continueFlow(key: String, value: Parcelable) {
        binding?.saveMapsAddress?.setOnClickListener {
            supportMapsFragment.getMapAsync { googleMaps ->
                findNavigation()?.show(fragment = AddressInfoFragment().apply {
                    arguments = Bundle().also { bundle ->
                        bundle.putParcelable(key, value)
                        bundle.putParcelable(AddressInfoFragment.LOCATION, googleMaps.cameraPosition.target)
                    }
                })
            }
        }
    }

}