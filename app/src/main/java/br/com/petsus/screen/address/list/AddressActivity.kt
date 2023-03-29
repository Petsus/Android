package br.com.petsus.screen.address.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import br.com.petsus.databinding.ActivityAddressBinding
import br.com.petsus.screen.address.AddressViewModel
import br.com.petsus.screen.address.add.AddUpdateAddressActivity
import br.com.petsus.screen.address.list.adapter.DeleteAddressHelper
import br.com.petsus.screen.address.list.adapter.ListAddressAdapter
import br.com.petsus.util.base.activity.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : BaseActivity() {

    private val viewModel: AddressViewModel by viewModels()

    private val binding: ActivityAddressBinding by lazy {
        ActivityAddressBinding.inflate(layoutInflater)
    }

    private val adapter: ListAddressAdapter = ListAddressAdapter().apply {
        addClickListener { editAddress ->
            startActivity(
                Intent(this@AddressActivity, AddUpdateAddressActivity::class.java)
                    .putExtra(AddUpdateAddressActivity.EXTRA_ADDRESS, editAddress)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configureView()
    }

    private fun configureView() {
        binding.listAddress.adapter = adapter
        binding.listAddress.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        binding.back.setOnClickListener { finish() }

        binding.addAddress.setOnClickListener {
            startActivity(
                Intent(this, AddUpdateAddressActivity::class.java)
            )
        }

        val helper = DeleteAddressHelper(this, object : DeleteAddressHelper.HelperCallback {
            override suspend fun canDelete(position: Int): Boolean = viewModel.delete(adapter.find(position).first())
            override fun removeItem(position: Int) = adapter.delete(adapter.find(position))
            override fun updateItem(position: Int) = adapter.notifyItemChanged(position)
        })

        ItemTouchHelper(helper)
            .attachToRecyclerView(binding.listAddress)

        viewModel.list().observe(this) { address ->
            adapter.put(address)
        }
    }

}