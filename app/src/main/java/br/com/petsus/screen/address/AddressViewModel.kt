package br.com.petsus.screen.address

import android.app.Application
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.petsus.api.model.address.AppAddress
import br.com.petsus.api.model.address.CreateOrUpdateAddress
import br.com.petsus.api.service.user.AddressRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.addressFromLocation
import br.com.petsus.util.tool.baseFlow
import br.com.petsus.util.tool.collector
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class AddressViewModel @Inject constructor(
    application: Application
) : AppViewModel(application = application) {

    @Inject
    lateinit var addressRepository: AddressRepository

    private val placesClient by lazy {
        Places.createClient(getApplication())
    }

    private val searchFlow: MutableStateFlow<List<AutocompletePrediction>> = MutableStateFlow(emptyList())
    private val addressChangeFlow: MutableStateFlow<AppAddress?> = MutableStateFlow(null)

    private var searchJob: Job? = null
    private var cancellationTokenSource: CancellationTokenSource? = null

    fun searchListener() = liveData {
        searchFlow.collector(this, viewModel = this@AddressViewModel)
    }

    fun list() = liveData {
        addressRepository.list()
            .collector(this, viewModel = this@AddressViewModel)
    }

    fun delete(appAddress: AppAddress) = liveData {
        addressRepository.delete(appAddress = appAddress)
            .collector(this, viewModel = this@AddressViewModel)
    }

    fun findAddress(prediction: AutocompletePrediction) = liveData {
        runCatching {
            suspendCoroutine { continuation ->
                placesClient.fetchPlace(
                    FetchPlaceRequest.newInstance(prediction.placeId, listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.PLUS_CODE))
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        val latLng = result.result.place.latLng ?: return@addOnCompleteListener
                        viewModelScope.launch {
                            runCatching {
                                val address = getApplication<Application>().addressFromLocation(latLng = latLng) ?: throw Throwable()
                                continuation.resume(address)
                            }
                        }
                    }
                }
            }
        }.onSuccess { emit(it) }
    }

    fun save(appAddress: CreateOrUpdateAddress) = liveData {
        addressRepository.save(appAddress = appAddress)
            .collector(this, viewModel = this@AddressViewModel)
    }

    fun search(text: String?) {
        searchJob?.cancel()
        cancellationTokenSource?.cancel()
        if (text == null || text.length < 3)
            return
        searchJob = viewModelScope.launch(viewModelScope.coroutineContext) {
            delay(3000)

            cancellationTokenSource = CancellationTokenSource()
            val request = FindAutocompletePredictionsRequest.builder()
                .setCancellationToken(cancellationTokenSource?.token)
                .setQuery(text)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnCompleteListener { taskResult ->
                    if (taskResult.isSuccessful)
                        viewModelScope.launch { searchFlow.emit(taskResult.result?.autocompletePredictions?.toList() ?: emptyList()) }
                }
        }
    }

    fun observerAddressChange() = liveData {
        addressChangeFlow.collector(this, viewModel = this@AddressViewModel)
    }

    fun notifyAddressChange(address: AppAddress) {
        viewModelScope.launch { addressChangeFlow.emit(address) }
    }

}