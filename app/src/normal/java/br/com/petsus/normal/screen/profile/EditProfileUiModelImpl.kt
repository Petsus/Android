package br.com.petsus.normal.screen.profile

import br.com.petsus.R
import br.com.petsus.screen.profile.EditProfileUiModel
import br.com.petsus.util.base.viewmodel.StringFormatter
import javax.inject.Inject

class EditProfileUiModelImpl @Inject constructor(): EditProfileUiModel {
    override val invalidName: StringFormatter
        get() = R.string.invalid_name.message()
    override val invalidEmail: StringFormatter
        get() = R.string.invalid_email.message()
    override val invalidPhone: StringFormatter
        get() = R.string.invalid_phone.message()

    private fun Int.message() = StringFormatter(messageId = this)
}