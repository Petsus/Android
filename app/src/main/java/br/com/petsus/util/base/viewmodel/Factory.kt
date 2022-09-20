package br.com.petsus.util.base.viewmodel

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import br.com.petsus.AppApplication
import br.com.petsus.api.service.APIRepository
import br.com.petsus.util.tool.cast

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.appViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
): Lazy<VM> = viewModels(extrasProducer) { ViewModelFactory(application) }

@MainThread
inline fun <reified VM : ViewModel> Fragment.appViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
): Lazy<VM> = createViewModelLazy(
    VM::class, { requireActivity().viewModelStore },
    { extrasProducer?.invoke() ?: requireActivity().defaultViewModelCreationExtras },
    { ViewModelFactory(requireActivity().application) }
)

class ViewModelFactory(private val application: Application) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        return modelClass.getConstructor(APIRepository::class.java).newInstance(application.cast<AppApplication>().repository)
    }
}