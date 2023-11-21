package br.com.petsus.screen.notification.dialog

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import br.com.petsus.api.model.notifications.NotificationsAnimal
import br.com.petsus.api.service.notification.NotificationRepository
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.tool.addressFromLocation
import br.com.petsus.util.tool.collector
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class DialogInfoFoundAnimalViewModel @Inject constructor(
    application: Application
) : AppViewModel(application) {
    @Inject
    lateinit var repository: NotificationRepository
    fun getDetails(notificationId: String): LiveData<NotificationsAnimal> = liveData {
        repository.getDetailsNotificationAnimal(notificationId)
            .collector(this, viewModel = this@DialogInfoFoundAnimalViewModel)
    }

    fun getAddressName(location: LatLng) = flow {
        val address = getApplication<Application>().addressFromLocation(location)?.getAddressLine(0)
        emit(address ?: "${location.latitude}, ${location.longitude}")
    }
}