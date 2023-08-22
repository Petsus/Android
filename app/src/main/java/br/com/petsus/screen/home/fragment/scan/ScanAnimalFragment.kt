package br.com.petsus.screen.home.fragment.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import br.com.petsus.databinding.FragmentScannerAnimalBinding
import br.com.petsus.screen.home.fragment.scan.dialog.ScanAnimalDialog
import br.com.petsus.util.base.fragment.AppFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanAnimalFragment : AppFragment<FragmentScannerAnimalBinding>() {
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if (result)
            binding?.scanner?.resume()
    }

    private val hasAuthorization: Boolean
        get() = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentScannerAnimalBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        if (hasAuthorization)
            binding?.apply{
                scanner.resume()
                scanner.decodeSingle { barcodeResult ->
                    val animalIdentifier = barcodeResult?.result?.text ?: return@decodeSingle
                    ScanAnimalDialog(animalIdentifier)
                        .show(childFragmentManager, null)
                }
            }
        else
            requestPermission.launch(Manifest.permission.CAMERA)
    }

    override fun onPause() {
        super.onPause()
        binding?.scanner?.pause()
    }
}