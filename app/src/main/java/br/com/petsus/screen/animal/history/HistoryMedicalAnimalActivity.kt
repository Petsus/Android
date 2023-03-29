package br.com.petsus.screen.animal.history

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.databinding.ActivityHistoryMedicalBinding
import br.com.petsus.util.tool.parcelable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryMedicalAnimalActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ANIMAL = "extra_animal"
    }

    private val viewModel: HistoryMedicalAnimalViewModel by viewModels()

    private val binding: ActivityHistoryMedicalBinding by lazy {
        ActivityHistoryMedicalBinding.inflate(layoutInflater)
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

    private fun configureView(animal: Animal) {
        binding.back.setOnClickListener { finish() }
        binding.addHistory.setOnClickListener {

        }
    }

}