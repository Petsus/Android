package br.com.petsus.local.application

import android.app.Application
import br.com.petsus.application.StarterApplication
import br.com.petsus.application.preferences.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

class StarterApplicationImpl : StarterApplication {
    override fun load(application: Application) {
    }
}

class AppPreferencesImpl : AppPreferences {
    companion object {
        val shared: AppPreferences = AppPreferencesImpl()
    }

    private val map: HashMap<String, Any?> = hashMapOf()

    override fun putObject(key: String, value: Any?) {
        map[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getObject(key: String, clazz: Class<T>): T? {
        return map[key] as? T
    }

}

@Module
@InstallIn(SingletonComponent::class)
class StarterApplicationModule {
    @Provides
    fun application(): StarterApplication = StarterApplicationImpl()

    @Provides
    fun preferences(): AppPreferences = AppPreferencesImpl.shared
}