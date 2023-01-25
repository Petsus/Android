package br.com.petsus.screen.home.fragment.profile

import androidx.lifecycle.ViewModel
import br.com.petsus.api.service.auth.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var sessionRepository: SessionRepository

    fun logout() {
        sessionRepository.token = null
    }
}