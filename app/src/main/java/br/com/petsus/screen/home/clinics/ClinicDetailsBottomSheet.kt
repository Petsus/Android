package br.com.petsus.screen.home.clinics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.petsus.databinding.BottomFragmentClinicDetailsBinding
import br.com.petsus.util.base.viewmodel.appViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ClinicDetailsBottomSheet(
    private val clinicId: Int
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomFragmentClinicDetailsBinding

    private val viewModel: ClinicViewModel by appViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomFragmentClinicDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loading.show()

        viewModel.findClinic(clinicId).observe(viewLifecycleOwner) { clinic ->

        }
    }

}