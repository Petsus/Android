package br.com.petsus.local.api.service.user

import br.com.petsus.api.service.user.AddressRepository
import br.com.petsus.api.service.user.UserRepository
import br.com.petsus.local.api.service.user.repository.AddressRepositoryImpl
import br.com.petsus.local.api.service.user.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UserModule {
    @Provides
    fun address(): AddressRepository = AddressRepositoryImpl()

    @Provides
    fun user(): UserRepository = UserRepositoryImpl()
}
