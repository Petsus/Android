package br.com.petsus.normal.application

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import br.com.petsus.application.preferences.AppPreferences
import br.com.petsus.util.tool.Keys
import com.google.gson.Gson

class AppPreferencesImpl(
    private val sharedPreferences: SharedPreferences
) : AppPreferences {
    companion object {
        lateinit var shared: AppPreferences

        fun create(context: Context) {
            synchronized(this) {
                shared = AppPreferencesImpl(
                    sharedPreferences = EncryptedSharedPreferences.create(
                        Keys.APP_PREFERENCES.valueKey,
                        Keys.APP_PREFERENCES.valueKey,
                        context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )
                )
            }
        }
    }

    private val gson = Gson()

    override fun putObject(key: String, value: Any?) {
        sharedPreferences.edit(commit = true) {
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