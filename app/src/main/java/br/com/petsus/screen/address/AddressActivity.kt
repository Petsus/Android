package br.com.petsus.screen.address

import android.os.Bundle
import br.com.petsus.databinding.ActivityAddressBinding
import br.com.petsus.screen.address.fragment.AddressListFragment
import br.com.petsus.util.base.activity.AppActivity
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.global.router.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : AppActivity() {

    private val viewModel: AddressViewModel by appViewModels()

    private val binding: ActivityAddressBinding by lazy {
        ActivityAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.observerAddressChange().observe(this) { newAddress ->
            if (newAddress != null)
                Navigator.of(binding.root).show(fragment = AddressListFragment(), addToBack = false)
        }

        Navigator.of(binding.root)
            .show(fragment = AddressListFragment(), addToBack = false)
    }
}