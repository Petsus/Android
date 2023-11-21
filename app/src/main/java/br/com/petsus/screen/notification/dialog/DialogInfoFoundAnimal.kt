package br.com.petsus.screen.notification.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import br.com.petsus.R
import br.com.petsus.databinding.FragmentInfoFoundAnimalBinding
import br.com.petsus.util.base.fragment.BaseBottomSheetDialogFragment
import br.com.petsus.util.base.viewmodel.appViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogInfoFoundAnimal(
    private val notificationID: String
) : BaseBottomSheetDialogFragment() {
    private val viewModel: DialogInfoFoundAnimalViewModel by appViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentInfoFoundAnimalBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentInfoFoundAnimalBinding.bind(view)) {
            loadingNotificationDetails.show()
            viewModel.getDetails(notificationID).observe(viewLifecycleOwner) { details ->
                loadingNotificationDetails.hide()
                containerInfoDetailsNotification.isVisible = true

                titleNotificationDetails.text = getString(R.string.your_animal_found, details.name)
                subtitleNotificationDetails.text = details.address

                buttonClose.setOnClickListener { dismiss() }
                buttonRedirect.setOnClickListener {
                    startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("geo:${details.lat},${details.lng}"))
                    )
                }

                containerMapsNotification.getFragment<SupportMapFragment>().getMapAsync { googleMaps ->
                    googleMaps.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(details.latLng, 16f)
                    )
                    googleMaps.addMarker(
                        MarkerOptions()
                            .title(details.name)
                            .position(details.latLng)
                    )
                }
            }
        }
    }
}