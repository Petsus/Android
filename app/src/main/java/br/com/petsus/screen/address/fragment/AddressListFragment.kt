package br.com.petsus.screen.address.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import br.com.petsus.databinding.FragmentAddressListBinding
import br.com.petsus.screen.address.AddressViewModel
import br.com.petsus.screen.address.adapter.ListAddressAdapter
import br.com.petsus.screen.address.adapter.ListSearchAdapter
import br.com.petsus.util.base.adapter.DeleteItemHelper
import br.com.petsus.util.base.adapter.RemoveCallback
import br.com.petsus.util.base.fragment.AppFragment
import br.com.petsus.util.base.fragment.findNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressListFragment : AppFragment<FragmentAddressListBinding>() {

    private val viewModel: AddressViewModel by activityViewModels()

    private val adapterListAddress: ListAddressAdapter = ListAddressAdapter().apply {
        addClickListener { editAddress ->
            findNavigation()?.show(
                AddressMapsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(AddressMapsFragment.ADDRESS, editAddress)
                    }
                }
            )
        }
    }

    private val adapterSearchAddress: ListSearchAdapter = ListSearchAdapter().apply {
        addClickListener { searchAddress ->
            viewModel.findAddress(prediction = searchAddress).observe(viewLifecycleOwner) { address ->
                findNavigation()?.show(
                    AddressMapsFragment().apply {
                        arguments = Bundle().apply {
                            putParcelable(AddressMapsFragment.SEARCH_ADDRESS, address)
                        }
                    }
                )
            }
        }
    }

    private val itemTouchHelper by lazy {
        ItemTouchHelper(
            DeleteItemHelper(requireContext(), object : DeleteItemHelper.HelperCallback {
                override fun canDelete(position: Int, callbackDelete: RemoveCallback) {
                    showLoading()
                    viewModel.delete(adapterListAddress.find(position).first()).observe(this@AddressListFragment) { result ->
                        closeLoading()
                        callbackDelete(result)
                    }
                }
                override fun removeItem(position: Int) = adapterListAddress.delete(adapterListAddress.find(position))
                override fun updateItem(position: Int) = adapterListAddress.notifyItemChanged(position)
            })
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddressListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.configureView()
    }

    private fun FragmentAddressListBinding.configureView() {
        listAddress.adapter = adapterListAddress
        itemTouchHelper.attachToRecyclerView(listAddress)

        listAddress.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        appBarAddress.setOnBackClickListener { activity?.finish() }

        searchAddress.editText?.addTextChangedListener(afterTextChanged = { editable ->
            val text = editable?.toString()
            when {
                text.isNullOrEmpty() && listAddress.adapter is ListSearchAdapter -> {
                    listAddress.adapter = adapterListAddress
                    itemTouchHelper.attachToRecyclerView(null)
                }
                !text.isNullOrEmpty() && listAddress.adapter is ListAddressAdapter -> {
                    listAddress.adapter = adapterSearchAddress
                    itemTouchHelper.attachToRecyclerView(listAddress)
                }
            }

            viewModel.search(text = text)
        })

        viewModel.searchListener().observe(viewLifecycleOwner, adapterSearchAddress::update)
        viewModel.list().observe(viewLifecycleOwner, adapterListAddress::update)
    }

}