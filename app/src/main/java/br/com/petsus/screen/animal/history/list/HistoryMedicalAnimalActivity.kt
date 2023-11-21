package br.com.petsus.screen.animal.history.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.databinding.ActivityHistoryMedicalBinding
import br.com.petsus.screen.animal.history.HistoryMedicalAnimalViewModel
import br.com.petsus.screen.animal.history.add.AddHistoryMedicalAnimalActivity
import br.com.petsus.screen.animal.history.list.adapter.HistoryMedicalAdapter
import br.com.petsus.util.base.activity.AppActivity
import br.com.petsus.util.base.adapter.DeleteItemHelper
import br.com.petsus.util.base.adapter.RemoveCallback
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.tool.parcelable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryMedicalAnimalActivity : AppActivity() {

    companion object {
        const val EXTRA_ANIMAL = "extra_animal"
    }

    private val viewModel: HistoryMedicalAnimalViewModel by appViewModels()

    private val binding: ActivityHistoryMedicalBinding by lazy {
        ActivityHistoryMedicalBinding.inflate(layoutInflater)
    }

    private val adapter = HistoryMedicalAdapter()

    private val itemTouchHelper by lazy {
        ItemTouchHelper(
            DeleteItemHelper(this, object : DeleteItemHelper.HelperCallback {
                override fun canDelete(position: Int, callbackDelete: RemoveCallback) {
                    showLoading()
                    viewModel.delete(adapter.find(position).first()).observe(this@HistoryMedicalAnimalActivity) { result ->
                        closeLoading()
                        callbackDelete(result)
                    }
                }
                override fun removeItem(position: Int) = adapter.delete(adapter.find(position))
                override fun updateItem(position: Int) = adapter.notifyItemChanged(position)
            })
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animal = intent.parcelable(EXTRA_ANIMAL, Animal::class.java) ?: run {
            finish()
            return
        }

        setContentView(binding.root)
        configureView(animal = animal)
    }

    override fun onStart() {
        super.onStart()
        viewModel.history().observe(this, adapter::update)
    }

    private fun configureView(animal: Animal) {
        binding.back.setOnClickListener { finish() }
        binding.listHistoryMedical.adapter = adapter

        itemTouchHelper.attachToRecyclerView(binding.listHistoryMedical)
        binding.listHistoryMedical.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        binding.addHistory.setOnClickListener {
            startActivity(
                Intent(this, AddHistoryMedicalAnimalActivity::class.java)
                    .putExtra(AddHistoryMedicalAnimalActivity.EXTRA_ANIMAL, animal)
            )
        }
    }
}