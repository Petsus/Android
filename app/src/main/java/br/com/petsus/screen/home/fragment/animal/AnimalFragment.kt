package br.com.petsus.screen.home.fragment.animal

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import br.com.petsus.databinding.FragmentAnimalBinding
import br.com.petsus.screen.animal.create.CreateAnimalActivity
import br.com.petsus.screen.animal.update.UpdateAnimalActivity
import br.com.petsus.screen.home.HomeViewModel
import br.com.petsus.util.base.adapter.DeleteItemHelper
import br.com.petsus.util.base.adapter.RemoveCallback
import br.com.petsus.util.base.fragment.AppFragment
import br.com.petsus.util.base.viewmodel.appActivityViewModels
import br.com.petsus.util.global.ResultState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

@AndroidEntryPoint
class AnimalFragment (
    private val defaultProvider: MainCoroutineDispatcher = Dispatchers.Main
) : AppFragment<FragmentAnimalBinding>() {

    private val requestNewAnimalActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result != null && result.resultCode == RESULT_OK)
            viewModel.getAnimals()
    }

    private val adapter: AnimalHomeAdapter = AnimalHomeAdapter().apply {
        addClickListener { animal ->
            startActivity(
                Intent(requireContext(), UpdateAnimalActivity::class.java)
                    .putExtra(UpdateAnimalActivity.EXTRA_ANIMAL, animal)
            )
        }
    }

    private val itemTouchHelper by lazy {
        ItemTouchHelper(
            DeleteItemHelper(requireContext(), object : DeleteItemHelper.HelperCallback {
                override fun canDelete(position: Int, callbackDelete: RemoveCallback) {
                    showLoading()
                    val response = viewModel.deleteAnimal(adapter.find(position).first()).observe(this@AnimalFragment) { result ->
                        closeLoading()
                        callbackDelete(result)
                    }
                }
                override fun removeItem(position: Int) = adapter.delete(adapter.find(position))
                override fun updateItem(position: Int) = adapter.notifyItemChanged(position)
            })
        )
    }

    private val viewModel: HomeViewModel by appActivityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadAnimals()
    }

    private fun loadAnimals() {
        viewModel.animals().observe(this) { animals ->
            when (animals) {
                is ResultState.Fail -> binding?.loadingAnimalHome?.hide()
                is ResultState.Init -> {
                    binding?.loadingAnimalHome?.show()
                    binding?.listAnimal?.isInvisible = true
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
        itemTouchHelper.attachToRecyclerView(binding?.listAnimal)

        binding?.apply {
            listAnimal.adapter = adapter
            listAnimal.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        binding?.addNewAnimal?.setOnClickListener {
            requestNewAnimalActivity.launch(Intent(requireContext(), CreateAnimalActivity::class.java))
        }
    }

}