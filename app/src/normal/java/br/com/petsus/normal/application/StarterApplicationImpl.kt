package br.com.petsus.normal.application

import android.app.Application
import br.com.petsus.application.StarterApplication
import br.com.petsus.application.preferences.AppPreferences
import br.com.petsus.normal.api.manager.ApiManager
import br.com.petsus.normal.api.manager.SessionManager
import br.com.petsus.service.publisher.UserPublisher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

class StarterApplicationImpl : StarterApplication {
    override fun load(application: Application) {
        AppPreferencesImpl.create(application)
        ApiManager.initialize(
            sessionManager = SessionManager.create(AppPreferencesImpl.shared, ApiManager.BaseInterceptor())
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
class StarterApplicationModule {
    @Provides
    fun application(): StarterApplication = StarterApplicationImpl()

    @Provides
    fun preferences(): AppPreferences = AppPreferencesImpl.shared

    @Provides
    @Singleton
    fun userPublisher(preferences: AppPreferences, coroutineScope: CoroutineScope): UserPublisher = UserPublisherImpl(preferences, coroutineScope)

    @Provides
    fun coroutine(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
}