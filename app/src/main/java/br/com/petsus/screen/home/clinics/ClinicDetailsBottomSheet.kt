package br.com.petsus.screen.home.clinics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import br.com.petsus.R
import br.com.petsus.databinding.BottomFragmentClinicDetailsBinding
import br.com.petsus.screen.home.clinics.pager.adapter.ClinicPagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClinicDetailsBottomSheet(
    private val clinicId: Long
) : BottomSheetDialogFragment() {

    private val viewModel: ClinicViewModel by viewModels()
    private lateinit var binding: BottomFragmentClinicDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomFragmentClinicDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadClinic()
        view.configureErrorListener()
    }

    private fun View.configureErrorListener() {
        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(this, R.string.cant_load_clinic, Snackbar.LENGTH_LONG).apply {
                setAction(R.string.retry) {
                    this.dismiss()
                    loadClinic()
                }
                show()
            }
        }
    }

    private fun loadClinic() {
        viewModel.find(clinicId).apply {
            binding.segmentMenuClinic.adapter = ClinicPagerAdapter(childFragmentManager, lifecycle, this)
            val names = resources.getStringArray(R.array.tabs_clinic_details)
            TabLayoutMediator(binding.tabMenuClinic, binding.segmentMenuClinic) { tab, position ->
                tab.text = names[position]
            }.attach()

        }.observe(viewLifecycleOwner) { clinic ->
            binding.clinic = clinic
            Glide.with(binding.imageClinic)
                .load(clinic.image)
                .fallback(R.drawable.logo)
                .into(binding.imageClinic)
                .clearOnDetach()
        }
    }

}