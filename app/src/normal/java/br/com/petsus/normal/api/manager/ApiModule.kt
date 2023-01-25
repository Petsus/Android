package br.com.petsus.normal.api.manager

import br.com.petsus.api.service.auth.SessionRepository
import br.com.petsus.normal.api.service.auth.SessionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class, ActivityComponent::class)
class ApiModule {
    @Provides
    fun session(): SessionRepository = SessionRepositoryImpl()
}