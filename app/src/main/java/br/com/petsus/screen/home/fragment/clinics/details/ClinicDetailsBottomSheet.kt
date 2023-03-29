package br.com.petsus.screen.home.fragment.clinics.details

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import br.com.petsus.R
import br.com.petsus.api.model.animal.Specie
import br.com.petsus.api.model.clinic.Exam
import br.com.petsus.databinding.BottomFragmentClinicDetailsBinding
import br.com.petsus.screen.home.fragment.clinics.ClinicViewModel
import br.com.petsus.screen.home.fragment.clinics.pager.ClinicChips
import br.com.petsus.screen.home.fragment.clinics.pager.ClinicInfo
import br.com.petsus.util.global.router.Navigator
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClinicDetailsBottomSheet(
    private val clinicId: Long
) : BottomSheetDialogFragment() {

    private val viewModel: ClinicViewModel by viewModels()
    private lateinit var binding: BottomFragmentClinicDetailsBinding

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomFragmentClinicDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadClinic()
    }

    private fun loadClinic() {
        binding.loadingClinic.show()

        binding.closeDialog.setOnClickListener { dismiss() }

        viewModel.find(clinicId).observe(viewLifecycleOwner) { clinic ->
            binding.clinic = clinic
            binding.loadingClinic.hide()
            binding.containerInfoClinic.isVisible = true

            val species = clinic.species.map(Specie::name)
            val exams = clinic.exams.map(Exam::name)

            Glide.with(requireContext())
                .load(clinic.image)
                .fallback(R.drawable.logo)
                .into(binding.imageClinic)
                .clearOnDetach()

            val navigator = Navigator.of(binding.frameClinic, childFragmentManager)
            navigator.present(fragment = ClinicInfo(clinic = clinic))

            binding.groupButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (!isChecked) return@addOnButtonCheckedListener

                when (checkedId) {
                    R.id.information_clinic -> navigator.present(fragment = ClinicInfo(clinic = clinic))
                    R.id.species_clinic -> navigator.present(fragment = ClinicChips(names = species))
                    R.id.exams_clinic -> navigator.present(fragment = ClinicChips(names = exams))
                    else -> throw NotImplementedError()
                }
            }
        }
    }
}