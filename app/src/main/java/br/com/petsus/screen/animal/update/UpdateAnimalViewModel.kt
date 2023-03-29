package br.com.petsus.screen.animal.update

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.global.ResultState
import br.com.petsus.util.tool.collector
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateAnimalViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var repository: AnimalRepository

    fun races(animal: Animal) = liveData {
        repository.races(animal = animal)
            .collector(this)
    }

    fun generateQrCode(value: String): Bitmap? {
        val formatWriter = MultiFormatWriter()
        return runCatching {
            BarcodeEncoder().createBitmap(
                formatWriter.encode(value, BarcodeFormat.QR_CODE, 1080, 1080)
            )
        }.getOrNull()
    }

    fun register() = liveData {
        repository.registerQrCode()
            .collector(this)
    }

    fun unregister(qrCode: String) = liveData {
        repository.unregisterQrCode(qrCode = qrCode)
            .collector(this)
    }

    fun lastFiveRecords() = liveData {
        repository.historyMedical()
            .transform {list ->
                this.emit(list.subList(0, 5))
            }
            .collector(this)

    }

    @Suppress("DEPRECATION")
    fun updateImage(uri: Uri) = liveData<ResultState<Bitmap?>> {
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
            repository.updateImage(fileImage = img.second)
                .transform { emit(ResultState.Success(if (it) img.first else null)) }
                .collector(this)
        }.onFailure { error ->
            emit(ResultState.Fail(StringFormatter(throwable = error)))
        }
    }

    fun updateAnimal(animal: Animal) = liveData {
        if (!animal.validate()) {
            emit(value = false)
            return@liveData
        }
        repository.updateAnimal(animal = animal)
            .collector(this)
    }

}