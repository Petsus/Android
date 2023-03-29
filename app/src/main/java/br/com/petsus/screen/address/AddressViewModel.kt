package br.com.petsus.screen.address

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.petsus.api.model.address.Address
import br.com.petsus.api.service.user.AddressRepository
import br.com.petsus.util.tool.collector
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application = application) {

    @Inject
    lateinit var addressRepository: AddressRepository

    private val placesClient by lazy {
        Places.createClient(getApplication())
    }

    private val searchFlow: MutableStateFlow<List<AutocompletePrediction>> = MutableStateFlow(emptyList())

    private var searchJob: Job? = null
    private val cancellationTokenSource = CancellationTokenSource()

    fun searchListener() = liveData {
        searchFlow.collector(this)
    }

    fun list() = liveData {
        addressRepository.list()
            .collector(this)
    }

    suspend fun delete(address: Address): Boolean {
        return addressRepository.delete(address = address)
            .single()
    }

    fun search(text: String?) {
        searchJob?.cancel()
        cancellationTokenSource.cancel()
        if (text == null || text.length < 3)
            return
        searchJob = viewModelScope.launch(SupervisorJob()) {
            delay(3000)
            val request = FindAutocompletePredictionsRequest.builder()
                .setCancellationToken(cancellationTokenSource.token)
                .setQuery(text)
                .build()
            placesClient.findAutocompletePredictions(request)
                .addOnCompleteListener { taskResult ->
                    if (taskResult.isSuccessful)
                        viewModelScope.launch { searchFlow.emit(taskResult.result?.autocompletePredictions?.toList() ?: emptyList()) }
                }
        }
    }

}