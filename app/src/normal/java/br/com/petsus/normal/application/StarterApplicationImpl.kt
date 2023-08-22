package br.com.petsus.normal.application

import android.app.Application
import br.com.petsus.application.StarterApplication
import br.com.petsus.application.preferences.AppPreferences
import br.com.petsus.normal.api.manager.ApiManager
import br.com.petsus.normal.api.manager.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
class StarterApplicationModule  {
    @Provides
    fun application(): StarterApplication = StarterApplicationImpl()

    @Provides
    fun preferences(): AppPreferences = AppPreferencesImpl.shared
}