package br.com.petsus.application

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AppApplication : Application() {

    @Inject lateinit var starterApplication: StarterApplication

    override fun onCreate() {
        super.onCreate()

        keyPlaces()?.apply {
            Places.initialize(applicationContext, this)
        }
        starterApplication.load(this)
    }

    @Suppress("DEPRECATION")
    private fun keyPlaces(): String? {
        return packageManager.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                return@run getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(0L))
             getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        }
            .metaData
            .getString("com.google.android.places.API_KEY")
    }

}