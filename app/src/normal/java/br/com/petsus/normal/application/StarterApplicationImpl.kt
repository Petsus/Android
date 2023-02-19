package br.com.petsus.normal.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.petsus.application.StarterApplication
import br.com.petsus.application.preferences.AppPreferences
import br.com.petsus.normal.api.manager.ApiManager
import br.com.petsus.normal.api.manager.SessionManager
import br.com.petsus.util.tool.Keys
import com.google.gson.Gson
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

class AppPreferencesImpl(
    private val sharedPreferences: SharedPreferences
) : AppPreferences {
    companion object {
        lateinit var shared: AppPreferences

        fun create(context: Context) {
            shared = AppPreferencesImpl(
                sharedPreferences = context.getSharedPreferences(Keys.APP_PREFERENCES.valueKey, Context.MODE_PRIVATE)
            )
        }
    }

    private val gson = Gson()

    override fun putObject(key: String, value: Any?) {
        sharedPreferences.edit {
            when {
                value == null -> putString(key, null)
                value::class.java.isPrimitive -> putString(key, value.toString())
                else -> putString(key, gson.toJson(value))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getObject(key: String, clazz: Class<T>): T? {
        val valueItem = sharedPreferences.getString(key, null) ?: return null
        return when {
            clazz.isPrimitive -> {
                return when (clazz) {
                    Int::class.java -> valueItem.toIntOrNull() as? T
                    Long::class.java -> valueItem.toLongOrNull() as? T
                    Float::class.java -> valueItem.toFloatOrNull() as? T
                    Double::class.java -> valueItem.toDoubleOrNull() as? T
                    String::class.java -> valueItem as? T
                    else -> null
                }
            }
            else -> gson.fromJson(valueItem, clazz)
        }
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