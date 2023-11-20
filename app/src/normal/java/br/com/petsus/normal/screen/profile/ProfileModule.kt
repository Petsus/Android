package br.com.petsus.normal.screen.profile

import br.com.petsus.screen.profile.EditProfileUiModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProfileModule {
    @Binds
    abstract fun bindEditProfileUiModel(impl: EditProfileUiModelImpl): EditProfileUiModel
}