package br.com.petsus.screen.address.add

import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import br.com.petsus.databinding.ActivityAddAddressBinding
import br.com.petsus.screen.address.AddressViewModel
import br.com.petsus.util.base.activity.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior

class AddUpdateAddressActivity : BaseActivity() {

    companion object {
        const val EXTRA_ADDRESS = "address"
    }

    private val viewModel: AddressViewModel by viewModels()

    private val binding: ActivityAddAddressBinding by lazy {
        ActivityAddAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configureView()
    }

    private fun configureView() {
        //binding.back.setOnClickListener { finish() }

        viewModel.searchListener().observe(this) {

        }

        binding.searchAddress.editText?.addTextChangedListener(afterTextChanged = {
            viewModel.search(text = it?.toString())
        })
    }

}