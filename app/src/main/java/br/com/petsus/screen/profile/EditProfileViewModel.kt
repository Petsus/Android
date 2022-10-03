package br.com.petsus.screen.profile

import br.com.petsus.api.service.APIRepository
import br.com.petsus.util.base.viewmodel.ViewModelLiveData

class EditProfileViewModel(
    private val repository: APIRepository
) : ViewModelLiveData() {

    fun get() =
        repository.user().getUser().toLiveData()

    fun update() =
        repository.user().update().toLiveData()

}