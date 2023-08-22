package br.com.petsus.screen.animal.history.add

import android.os.Bundle
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.model.animal.CreateHistory
import br.com.petsus.databinding.ActivityAddHistoryMedicalAnimalBinding
import br.com.petsus.screen.animal.history.HistoryMedicalAnimalViewModel
import br.com.petsus.screen.animal.history.add.dialog.exam.SearchVaccineHistoryDialog
import br.com.petsus.screen.animal.history.add.dialog.vaccine.SearchExamHistoryDialog
import br.com.petsus.util.base.activity.AppActivity
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.tool.messageString
import br.com.petsus.util.tool.parcelable

class AddHistoryMedicalAnimalActivity : AppActivity() {
    companion object {
        const val EXTRA_ANIMAL = "extra_animal"
    }

    private val viewModel: HistoryMedicalAnimalViewModel by appViewModels()

    private val binding: ActivityAddHistoryMedicalAnimalBinding by lazy {
        ActivityAddHistoryMedicalAnimalBinding.inflate(layoutInflater)
    }

    private val builder = CreateHistory.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.parcelable(EXTRA_ANIMAL, Animal::class.java)?.let { animal ->
            builder.setAnimalId(animal.id)
        }

        binding.searchVaccine.editText?.showSoftInputOnFocus = false
        binding.searchVaccine.editText?.setOnClickListener {
            SearchVaccineHistoryDialog { vaccine ->
                builder.setVaccine(vaccine)
                binding.searchVaccine.editText?.setText(vaccine.name)
            }.show(supportFragmentManager, null)
        }

        binding.searchExam.editText?.showSoftInputOnFocus = false
        binding.searchExam.editText?.setOnClickListener {
            SearchExamHistoryDialog { exam ->
                builder.setExam(exam)
                binding.searchExam.editText?.setText(exam.name)
            }.show(supportFragmentManager, null)
        }

        binding.back.setOnBackClickListener { finish() }
        binding.saveHistoryMedical.setOnClickListener {
            builder.setVeterinary(binding.nameVeterinary.editText?.text?.toString() ?: "")
            runCatching {
                builder.build()
            }.onFailure { error ->
                this.error(error.messageString)
            }.onSuccess { history ->
                viewModel.create(history).observe(this) { finish() }
            }
        }
    }
}