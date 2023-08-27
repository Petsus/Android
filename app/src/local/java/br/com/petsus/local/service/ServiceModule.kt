package br.com.petsus.local.service

import br.com.petsus.service.publisher.UserPublisher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Singleton
    @Binds
    abstract fun bindUserPublisher(impl: UserPublisherImpl): UserPublisher
}