package br.com.petsus.screen.animal.history.add.dialog.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.petsus.api.model.clinic.Vaccine
import br.com.petsus.databinding.FragmentSearchValueBinding
import br.com.petsus.screen.animal.history.add.dialog.SimpleAdapter
import br.com.petsus.util.base.fragment.BaseBottomSheetDialogFragment
import br.com.petsus.util.base.viewmodel.appViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchVaccineHistoryDialog(
    private val callback: (item: Vaccine) -> Unit
) : BaseBottomSheetDialogFragment() {
    private var binding: FragmentSearchValueBinding? = null
    private val viewModel: SearchVaccineHistoryViewModel by appViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchValueBinding.inflate(inflater, container, false)
        return binding?.root

    }

    private val adapter: SimpleAdapter<Vaccine> = SimpleAdapter<Vaccine>().apply {
        addClickListener { item ->
            callback(item.second)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observer().observe(this) { items ->
            adapter.update(items)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.back?.setOnClickListener { dismiss() }
        binding?.listSearch?.adapter = adapter
        binding?.listSearch?.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        binding?.searchField?.editText?.addTextChangedListener(afterTextChanged = { text ->
            viewModel.search(text = text?.toString())
        })
    }
}