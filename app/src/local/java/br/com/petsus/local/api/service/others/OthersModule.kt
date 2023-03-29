package br.com.petsus.local.api.service.others

import br.com.petsus.api.service.others.AboutAppRepository
import br.com.petsus.api.service.others.NewsRepository
import br.com.petsus.local.api.service.others.repository.AboutAppRepositoryImpl
import br.com.petsus.local.api.service.others.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class OthersModule {
    @Provides
    fun news(): NewsRepository = NewsRepositoryImpl()

    @Provides
    fun about(): AboutAppRepository = AboutAppRepositoryImpl()
}