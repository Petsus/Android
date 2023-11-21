package br.com.petsus.screen.home.fragment.scan.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import br.com.petsus.databinding.FragmentScanInfoBinding
import br.com.petsus.util.base.fragment.BaseBottomSheetDialogFragment
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.global.dialog.LoadingFragment
import br.com.petsus.util.tool.latLng
import br.com.petsus.util.tool.preventDoubleClick
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanAnimalDialog(
    private val animalIdentifier: String
) : BaseBottomSheetDialogFragment() {
    private val viewModel: ScanAnimalViewModel by appViewModels()

    private val requestLocation = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        viewModel.handlerPermission(result)
    }

    private val requestEnableGPS = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        viewModel.handlerEnableGPS(result)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener { dialogInterface ->
                val bottomSheetDialog = dialogInterface as? BottomSheetDialog ?: return@setOnShowListener
                val bottomSheetView = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) ?: return@setOnShowListener
                BottomSheetBehavior.from(bottomSheetView).also { sheet ->
                    sheet.skipCollapsed = true
                    sheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentScanInfoBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentScanInfoBinding.bind(view)) {
            loadingScanInfo.show()

            root.doOnLayout {
                containerScan.minHeight = it.height
            }

            viewModel.getAnimal(animalIdentifier).observe(viewLifecycleOwner) { animal ->
                viewModel.verifyPermissionGPS(requestLocation) { isEnable ->
                    if (isEnable)
                        viewModel.enableGPSAndLoadLocation(requestEnableGPS)
                }

                nameAnimal.text = animal.name

                viewModel.location().observe(viewLifecycleOwner) { currentLocation ->
                    loadingScanInfo.hide()
                    containerScan.isVisible = true
                    containerMapsScan.getFragment<SupportMapFragment>().getMapAsync { googleMap ->
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(currentLocation.latLng)
                                .title("You")
                        )
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation.latLng, 15f))
                    }

                    confirmAndContinue.setOnClickListener { button ->
                        button.preventDoubleClick()
                        this@ScanAnimalDialog.showLoading()
                        viewModel.sendAnimalFound(animal, currentLocation.latLng).observe(viewLifecycleOwner) {
                            this@ScanAnimalDialog.closeLoading {
                                this@ScanAnimalDialog.dismiss()
                            }
                        }
                    }
                }
            }
        }
    }
}