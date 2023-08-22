package br.com.petsus.util.base.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.petsus.util.base.dialog.ErrorInterface
import java.lang.ref.WeakReference

interface AppViewModelInterface {
    fun configure(errorInterface: ErrorInterface)
    fun notify(stringFormatter: StringFormatter)
}

abstract class AppViewModel(application: Application) : AndroidViewModel(application), AppViewModelInterface {
    private var weakErrorInterface: WeakReference<ErrorInterface>? = null

    override fun configure(errorInterface: ErrorInterface) {
        weakErrorInterface?.clear()
        weakErrorInterface = WeakReference(errorInterface)
    }

    override  fun notify(stringFormatter: StringFormatter) {
        weakErrorInterface?.get()?.error(stringFormatter)
    }

    override fun onCleared() {
        super.onCleared()
        weakErrorInterface = null
    }
}