package br.com.petsus.local.api.service.auth

import br.com.petsus.api.service.auth.AuthRepository
import br.com.petsus.api.service.auth.SessionRepository
import br.com.petsus.local.api.service.auth.repository.AuthRepositoryImpl
import br.com.petsus.local.api.service.auth.repository.SessionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class, ActivityComponent::class)
class AuthModule {
    @Provides
    fun auth(): AuthRepository = AuthRepositoryImpl()

    @Provides
    fun session(): SessionRepository = SessionRepositoryImpl()
}