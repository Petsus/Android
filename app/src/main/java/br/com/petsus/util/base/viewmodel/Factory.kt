package br.com.petsus.util.base.viewmodel

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
import br.com.petsus.util.base.dialog.ErrorInterface
import br.com.petsus.util.tool.cast

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.appViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
): Lazy<VM> = viewModels(extrasProducer) { ViewModelFactory(this, this as? ErrorInterface) }

@MainThread
inline fun <reified VM : ViewModel> Fragment.appViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
): Lazy<VM> = createViewModelLazy(
    VM::class, { requireActivity().viewModelStore },
    { extrasProducer?.invoke() ?: requireActivity().defaultViewModelCreationExtras },
    { ViewModelFactory(requireActivity(), this as? ErrorInterface) }
)

class ViewModelFactory(
    private val activity: ComponentActivity,
    private val errorInterface: ErrorInterface?
) : AbstractSavedStateViewModelFactory() {
    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        return modelClass.getConstructor(APIRepository::class.java).newInstance(activity.application.cast<AppApplication>().repository).apply {
            if (this is ViewModelLiveData && errorInterface != null) {
                this.error.observe(activity, errorInterface::error)
            }
        }
    }
}