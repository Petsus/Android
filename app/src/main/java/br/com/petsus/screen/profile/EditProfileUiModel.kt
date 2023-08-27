package br.com.petsus.screen.profile

import br.com.petsus.util.base.viewmodel.StringFormatter

interface EditProfileUiModel {
    val invalidName: StringFormatter
    val invalidEmail: StringFormatter
    val invalidPhone: StringFormatter
}