package br.com.petsus.screen.animal.update

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.liveData
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.api.service.animal.HistoryMedicalRepository
import br.com.petsus.api.service.user.AddressRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.global.ResultState
import br.com.petsus.util.tool.collector
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateAnimalViewModel @Inject constructor(application: Application) : AppViewModel(application) {

    @Inject
    lateinit var repository: AnimalRepository

    @Inject
    lateinit var addressesRepository: AddressRepository

    @Inject
    lateinit var historyMedicalRepository: HistoryMedicalRepository

    fun races(animal: Animal) = liveData {
        repository.races(animal = animal)
            .collector(this, viewModel = this@UpdateAnimalViewModel)
    }

    fun generateQrCode(value: String): Bitmap? {
        val formatWriter = MultiFormatWriter()
        return runCatching {
            BarcodeEncoder().createBitmap(
                formatWriter.encode(value, BarcodeFormat.QR_CODE, 1080, 1080)
            )
        }.getOrNull()
    }

    fun register(animal: Animal) = liveData {
        repository.registerQrCode(animal)
            .collector(this, viewModel = this@UpdateAnimalViewModel)
    }

    fun unregister(qrCode: String) = liveData {
        repository.unregisterQrCode(qrCode = qrCode)
            .collector(this, viewModel = this@UpdateAnimalViewModel)
    }

    fun lastFiveRecords() = liveData {
        historyMedicalRepository.history()
            .transform {list ->
                this.emit(list.subList(0, 5))
            }
            .collector(this, viewModel = this@UpdateAnimalViewModel)

    }

    @Suppress("DEPRECATION")
    fun updateImage(uri: Uri, animalId: Long) = liveData<ResultState<Bitmap?>> {
        emit(ResultState.Init())
        runCatching {
            val image = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> ImageDecoder.createSource(getApplication<Application>().contentResolver, uri).run(ImageDecoder::decodeBitmap)
                else -> MediaStore.Images.Media.getBitmap(getApplication<Application>().contentResolver, uri)
            }
            val file = File(getApplication<Application>().cacheDir, "animal.png").also { file ->
                withContext(Dispatchers.IO) { image.compress(Bitmap.CompressFormat.PNG, 100, file.outputStream()) }
            }
            return@runCatching image to file
        }.onSuccess { img ->
            repository.updateImage(animalId = animalId, fileImage = img.second)
                .transform { emit(ResultState.Success(if (it) img.first else null)) }
                .collector(this, viewModel = this@UpdateAnimalViewModel)
        }.onFailure { error ->
            emit(ResultState.Fail(StringFormatter(throwable = error)))
        }
    }

    fun updateAnimal(animal: Animal) = liveData {
        if (!animal.validate()) {
            delay(300)
            emit(value = false)
            return@liveData
        }
        repository.updateAnimal(animal = animal)
            .collector(this, viewModel = this@UpdateAnimalViewModel)
    }

    fun addresses() = liveData {
        addressesRepository.list()
            .collector(this, viewModel = this@UpdateAnimalViewModel)
    }

}