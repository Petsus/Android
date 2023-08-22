package br.com.petsus.screen.animal.create

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import br.com.petsus.R
import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.api.model.animal.CreateAnimal
import br.com.petsus.api.model.animal.Race
import br.com.petsus.api.model.animal.Specie
import br.com.petsus.databinding.ActivityCreateAnimalBinding
import br.com.petsus.util.base.activity.AppActivity
import br.com.petsus.util.base.viewmodel.MessageThrowable
import br.com.petsus.util.tool.cast
import br.com.petsus.util.tool.format
import br.com.petsus.util.tool.pixel
import br.com.petsus.util.tool.preventDoubleClick
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
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CreateAnimalActivity : AppActivity() {

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        loadImage(hasPermission = result)
    }

    private val hasAuthorization: Boolean
        get() = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private val requestNewImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result == null || result.resultCode != RESULT_OK)
            return@registerForActivityResult
        result.data?.data?.run(this::configureImage)
    }

    private val animalBuilder = CreateAnimal.Builder()

    private val binding: ActivityCreateAnimalBinding by lazy {
        ActivityCreateAnimalBinding.inflate(layoutInflater)
    }

    private val viewModel: CreateAnimalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureView()
    }

    private fun configureView() {
        binding.back.setOnClickListener { finish() }
        binding.imageAnimal.setOnClickListener { imageView ->
            imageView.preventDoubleClick()
            when {
                hasAuthorization -> loadImage(hasPermission = true)
                else -> requestPermission.launch(Manifest.permission.CAMERA)
            }
        }

        binding.birthdayAnimal.editText?.showSoftInputOnFocus = false
        binding.birthdayAnimal.editText?.setOnClickListener {
            MaterialDatePicker.Builder.datePicker()
                .setNegativeButtonText(R.string.cancel)
                .setPositiveButtonText(R.string.ok)
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setEnd(Date().time)
                        .build()
                )
                .setTextInputFormat(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))
                .setTitleText(R.string.select_birthday)
                .build()
                .apply {
                    addOnPositiveButtonClickListener { time ->
                        val date = Date(time).format("dd/MM/yyyy")
                        binding.birthdayAnimal.editText?.setText(date)
                        animalBuilder.setBirthday(date)
                    }
                    show(supportFragmentManager, null)
                }
        }

        binding.saveChangesAnimal.setOnClickListener {
            runCatching { animalBuilder.build() }
                .onFailure { error -> this.error(error.cast<MessageThrowable>().appMessage) }
                .onSuccess { animal ->
                    showLoading()
                    viewModel.create(animal).observe(this) { finish() }
                }
        }

        binding.nameAnimal.editText?.addTextChangedListener(afterTextChanged = { text -> text?.toString()?.run(animalBuilder::setName) })
        binding.weightAnimal.editText?.addTextChangedListener(afterTextChanged = { text -> text?.toString()?.toFloat()?.run(animalBuilder::setWeight) })
        binding.heightAnimal.editText?.addTextChangedListener(afterTextChanged = { text -> text?.toString()?.toInt()?.run(animalBuilder::setHeight) })

        viewModel.addresses().observe(this) { address ->
            binding.addressAnimal.editText?.cast<MaterialAutoCompleteTextView>()?.apply {
                setSimpleItems(address.map(AppAddress::completeAddress).toTypedArray())
                setOnItemClickListener { _, _, position, _ ->
                    val addressId = address.getOrNull(position)?.id ?: return@setOnItemClickListener
                    this@CreateAnimalActivity.animalBuilder.setAddressId(addressId)
                }
            }
        }

        viewModel.species().observe(this) { species ->
            binding.specieAnimal.editText?.cast<MaterialAutoCompleteTextView>()?.apply {
                setSimpleItems(species.map(Specie::name).toTypedArray())
                setOnItemClickListener { _, _, position, _ ->
                    val specie = species.getOrNull(position) ?: return@setOnItemClickListener
                    viewModel.races(specie = specie).observe(this@CreateAnimalActivity) { races ->
                        binding.raceAnimal.editText?.cast<MaterialAutoCompleteTextView>()?.apply {
                            setSimpleItems(races.map(Race::name).toTypedArray())
                            setOnItemClickListener { _, _, positionRace, _ ->
                                races.getOrNull(positionRace)?.id?.let { raceId ->
                                    this@CreateAnimalActivity.animalBuilder.setRaceId(raceId)
                                }
                            }
                        }
                    }
                }
            }
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

    private fun configureImage(uri: Uri) {
        animalBuilder.setPhoto(uri)
        Glide.with(binding.imageAnimal)
            .asDrawable()
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(RoundedCorners(16f.pixel), CenterCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageAnimal)
            .waitForLayout()
            .clearOnDetach()
    }
}