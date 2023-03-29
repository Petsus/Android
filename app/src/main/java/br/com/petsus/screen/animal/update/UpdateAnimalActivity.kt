package br.com.petsus.screen.animal.update

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import br.com.petsus.R
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.model.animal.Race
import br.com.petsus.databinding.ActivityUpdateAnimalBinding
import br.com.petsus.screen.animal.history.HistoryMedicalAnimalActivity
import br.com.petsus.screen.animal.update.adapter.SimpleHistoryMedical
import br.com.petsus.screen.animal.update.sheet.QrCodeBottomSheet
import br.com.petsus.util.base.activity.BaseActivity
import br.com.petsus.util.global.ResultState
import br.com.petsus.util.tool.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UpdateAnimalActivity : BaseActivity() {

    companion object {
        const val EXTRA_ANIMAL = "key_animal"
    }

    private val binding: ActivityUpdateAnimalBinding by lazy {
        ActivityUpdateAnimalBinding.inflate(layoutInflater)
    }

    private val viewModel: UpdateAnimalViewModel by viewModels()

    private val requestNewImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result == null || result.resultCode != RESULT_OK)
            return@registerForActivityResult
        result.data?.data?.also { uri ->
            viewModel.updateImage(uri = uri).observe(this) { newImage ->
                when (newImage) {
                    is ResultState.Fail -> {
                        binding.loadingImage.hide()
                        binding.editImageAnimal.isClickable = true
                    }
                    is ResultState.Init -> {
                        binding.loadingImage.show()
                        binding.editImageAnimal.isClickable = false
                    }
                    is ResultState.Success -> {
                        binding.loadingImage.hide()
                        binding.editImageAnimal.isClickable = true
                        binding.imageAnimal.setImageBitmap(newImage.data)
                    }
                }
            }
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        loadImage(hasPermission = result)
    }

    private val hasAuthorization: Boolean
        get() = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private val races: MutableList<Race> = mutableListOf()

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
        binding.animal = animal
        binding.loadingImage.hide()
        binding.editImageAnimal.setOnClickListener {
            when {
                hasAuthorization -> loadImage(hasPermission = true)
                else -> requestPermission.launch(Manifest.permission.CAMERA)
            }
        }

        binding.seeQrCode.setOnClickListener {
            val qrCodeText = animal.qrcode ?: return@setOnClickListener
            val image = viewModel.generateQrCode(qrCodeText) ?: return@setOnClickListener
            QrCodeBottomSheet(image)
                .show(supportFragmentManager, null)
        }

        binding.birthdayAnimal.editText?.showSoftInputOnFocus = false
        binding.birthdayAnimal.editText?.setOnClickListener {
            MaterialDatePicker.Builder.datePicker()
                .setNegativeButtonText(R.string.cancel)
                .setPositiveButtonText(R.string.ok)
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setEnd(Date().time)
                        .setOpenAt(animal.birthday.toDate("dd/MM/yyyy")?.time ?: Date().time)
                        .build()
                )
                .setTextInputFormat(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))
                .setTitleText(R.string.select_birthday)
                .build()
                .apply {
                    addOnPositiveButtonClickListener { time ->
                        binding.birthdayAnimal.editText?.setText(Date(time).format("dd/MM/yyyy"))
                    }
                    show(supportFragmentManager, null)
                }
        }

        binding.registerOrUnregisterQrCode.setOnClickListener {
            animal.qrcode?.apply {
                viewModel.unregister(qrCode = this)
                    .observe(this@UpdateAnimalActivity) {
                        animal.currentQrCode = null
                    }
            } ?: run {
                viewModel.register()
                    .observe(this@UpdateAnimalActivity) { qrCode ->
                        animal.currentQrCode = qrCode
                    }
            }
        }

        binding.saveChangesAnimal.setOnClickListener {
            animal.name = binding.nameAnimal.text ?: ""
            animal.weight = binding.weightAnimal.text?.toFloatOrNull() ?: 0f
            animal.height = binding.heightAnimal.text?.toIntOrNull() ?: 0
            animal.birthday = binding.birthdayAnimal.text ?: ""
            binding.raceAnimal.text?.also { nameRace ->
                races.find { race -> race.name == nameRace }?.let { race ->
                    animal.race = race
                }
            }

            loading()
            viewModel.updateAnimal(animal = animal)
                .observe(this) {
                    closeLoading()
                }
        }

        binding.back.setOnClickListener { finish() }

        viewModel.lastFiveRecords().observe(this) { listHistory ->
            binding.recordMedicDataAnimal.isVisible = true
            binding.recordMedicHistoryAnimal.adapter = SimpleHistoryMedical().apply {
                put(listHistory)
            }
            binding.seeMoreHistoryMedicalAnimal.setOnClickListener {
                startActivity(
                    Intent(this, HistoryMedicalAnimalActivity::class.java)
                        .putExtra(HistoryMedicalAnimalActivity.EXTRA_ANIMAL, animal)
                )
            }
        }

        Glide.with(binding.imageAnimal)
            .asDrawable()
            .load(animal.photo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(RoundedCorners(16f.pixel), CenterCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageAnimal)
            .waitForLayout()
            .clearOnDetach()

        viewModel.races(animal = animal).observe(this) { races ->
            this.races.addAll(races)
            binding.raceAnimal.editText
                ?.cast<MaterialAutoCompleteTextView>()
                ?.setSimpleItems(races.map(Race::name).toTypedArray())
        }
    }

    private fun loadImage(hasPermission: Boolean) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            if (hasPermission)
                putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayListOf(cameraIntent))
        }

        requestNewImage.launch(Intent.createChooser(galleryIntent, "Select picture"))
    }

}