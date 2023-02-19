package br.com.petsus.screen.home.fragment.animal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.petsus.databinding.FragmentAnimalBinding
import br.com.petsus.screen.home.fragment.home.HomeViewModel
import br.com.petsus.util.base.fragment.BaseFragment
import br.com.petsus.util.global.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnimalFragment : BaseFragment<FragmentAnimalBinding>() {

    private val adapter: AnimalHomeAdapter = AnimalHomeAdapter().apply {
        addClickListener { animal ->

        }
    }

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.animals().observe(this) { animals ->
            when (animals) {
                is ResultState.Init -> {
                    binding?.loadingAnimalHome?.show()
                    binding?.listAnimal?.isInvisible = true
                }
                is ResultState.Fail -> {
                    binding?.loadingAnimalHome?.hide()
                }
                is ResultState.Success -> {
                    binding?.loadingAnimalHome?.hide()
                    binding?.listAnimal?.isInvisible = false
                    adapter.update(animals.data)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAnimalBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            listAnimal.adapter = adapter
            listAnimal.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

}