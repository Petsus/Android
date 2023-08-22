package br.com.petsus.screen.address.fragment

import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import br.com.petsus.R
import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.api.model.address.CreateOrUpdateAddress
import br.com.petsus.databinding.FragmentAddressInfoBinding
import br.com.petsus.screen.address.AddressViewModel
import br.com.petsus.util.base.fragment.AppFragment
import br.com.petsus.util.base.fragment.findNavigation
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.tool.parcelable
import br.com.petsus.util.tool.text
import com.google.android.gms.maps.model.LatLng

class AddressInfoFragment : AppFragment<FragmentAddressInfoBinding>() {
    companion object {
        const val ADDRESS = "data_address"
        const val LOCATION = "data_location"
        const val SEARCH_ADDRESS = "data_search_address"
    }

    private val viewModel: AddressViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddressInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            appBarAddressInfo.setOnBackClickListener { findNavigation()?.dismiss() }
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
        val appAddress = bundleArguments.parcelable(AddressMapsFragment.ADDRESS, AppAddress::class.java) ?: return run { findNavigation()?.dismiss() }
        val location = bundleArguments.parcelable(LOCATION, LatLng::class.java) ?: return run { findNavigation()?.dismiss() }

        binding?.apply {
            fieldNumberAddress.editText?.setText(appAddress.nickname)
            fieldThoroughfareAddress.editText?.setText(appAddress.address)
            fieldNumberAddress.editText?.setText(appAddress.number.toString())
            fieldComplementAddress.editText?.setText(appAddress.complement)
            fieldNeighborhoodAddress.editText?.setText(appAddress.neighborhood)

            saveAddress.setOnClickListener {
                val update = checkFields(location = location, id = appAddress.id, postalCode = appAddress.postalCode) ?: return@setOnClickListener
                showLoading()
                viewModel.save(appAddress = update).observe(viewLifecycleOwner) { updateAddress ->
                    closeLoading {
                        viewModel.notifyAddressChange(address = updateAddress)
                    }
                }
            }
        }
    }

    private fun loadSearchAddress(bundleArguments: Bundle) {
        val address = bundleArguments.parcelable(AddressMapsFragment.SEARCH_ADDRESS, Address::class.java) ?: return run { findNavigation()?.dismiss() }
        val location = bundleArguments.parcelable(LOCATION, LatLng::class.java) ?: return run { findNavigation()?.dismiss() }

        binding?.apply {
            fieldThoroughfareAddress.editText?.setText(address.thoroughfare)
            fieldNeighborhoodAddress.editText?.setText(address.subLocality)

            saveAddress.setOnClickListener {
                val update = checkFields(location = location, id = null, postalCode = address.postalCode) ?: return@setOnClickListener
                showLoading()
                viewModel.save(appAddress = update).observe(viewLifecycleOwner) { updateAddress ->
                    closeLoading()
                    viewModel.notifyAddressChange(address = updateAddress)
                }
            }
        }
    }

    private fun checkFields(location: LatLng, id: Long?, postalCode: String?): CreateOrUpdateAddress? = runCatching {
        val nickname = binding?.fieldNicknameAddress?.text
        val address = binding?.fieldThoroughfareAddress?.text
        val number = binding?.fieldNumberAddress?.text?.toIntOrNull()
        val neighborhood = binding?.fieldNeighborhoodAddress?.text
        val complement = binding?.fieldComplementAddress?.text

        require(!address.isNullOrEmpty()) { getString(R.string.error_address) }
        require(!nickname.isNullOrEmpty()) { getString(R.string.error_nickname) }
        require(!neighborhood.isNullOrEmpty()) { getString(R.string.error_neighborhood) }
        require(number != null) { getString(R.string.error_number_address) }

        return@runCatching CreateOrUpdateAddress(
            id = id,
            number = number,
            address = address,
            nickname = nickname,
            complement = complement,
            postalCode = postalCode,
            lat = location.latitude,
            lng = location.longitude,
            neighborhood = neighborhood
        )
    }.onFailure { error ->
        message(StringFormatter(messageString = error.message))
    }.getOrNull()
}