package br.com.petsus.screen.home

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.petsus.api.model.animal.Animal
import br.com.petsus.api.model.others.News
import br.com.petsus.api.service.animal.AnimalRepository
import br.com.petsus.api.service.auth.SessionRepository
import br.com.petsus.api.service.notification.NotificationRepository
import br.com.petsus.api.service.others.NewsRepository
import br.com.petsus.api.service.user.UserRepository
import br.com.petsus.service.publisher.UserPublisher
import br.com.petsus.util.base.viewmodel.AppViewModel
import br.com.petsus.util.global.ResultState
import br.com.petsus.util.tool.baseFlow
import br.com.petsus.util.tool.collector
import br.com.petsus.util.tool.collectorState
import com.bumptech.glide.load.model.GlideUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val publisher: UserPublisher
) : AppViewModel(application) {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var sessionRepository: SessionRepository

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @set:Inject
    var newsRepository: NewsRepository? = null
        set(value) {
            field = value
            viewModelScope.launch {
                value?.news()?.collectorState(stateFlow = flowStateNews)
            }
        }

    @set:Inject
    var animalRepository: AnimalRepository? = null
        set(value) {
            field = value
            viewModelScope.launch {
                value?.all()?.collectorState(stateFlow = flowStateAnimal)
            }
        }

    private val flowStateNews: MutableStateFlow<ResultState<List<News>>> = MutableStateFlow(ResultState.Init())

    private val flowStateAnimal: MutableStateFlow<ResultState<List<Animal>>> = MutableStateFlow(ResultState.Init())

    fun news() = flowStateNews.asLiveData()

    fun getAnimals() {
        viewModelScope.launch {
            animalRepository?.all()?.collectorState(stateFlow = flowStateAnimal)
        }
    }

    fun animals() = flowStateAnimal.asLiveData()

    fun name() = liveData {
        publisher.current
            .map { user -> user.name }
            .collector(
                liveDataScope = this,
                viewModel = this@HomeViewModel
            )
    }

    fun handlerImage(uri: Uri) = liveData {
        getApplication<Application>()
            .contentResolver
            .takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        userRepository.saveImage(uri)
            .baseFlow()
            .collect { isSaved ->
                if (isSaved)
                    emit(uri)
            }
    }

    fun currentImage() = liveData {
        userRepository.currentImage()
            .baseFlow(viewModel = this@HomeViewModel)
            .collect { url ->
                emit(url?.run { GlideUrl(this, sessionRepository.headersGlide()) })
            }
    }
    fun deleteAnimal(animal: Animal) = liveData {
        animalRepository?.deleteAnimal(animal)
            ?.collector(this, viewModel = this@HomeViewModel)
    }
    fun logout() {
        CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()).launch {
            val token = sessionRepository.token?.completeToken ?: return@launch
            notificationRepository.clearTokenNotification(token)
        }
        sessionRepository.token = null
    }

}