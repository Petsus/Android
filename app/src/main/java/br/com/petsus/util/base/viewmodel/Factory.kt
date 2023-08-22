package br.com.petsus.util.base.viewmodel

import androidx.annotation.MainThread
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import br.com.petsus.util.base.activity.AppActivity
import br.com.petsus.util.base.dialog.ErrorInterface
import br.com.petsus.util.base.fragment.AppFragment
import br.com.petsus.util.base.fragment.BaseBottomSheetDialogFragment
import br.com.petsus.util.tool.tryCast

class AppViewModelFactory(
    private val defaultFactory: () -> ViewModelProvider.Factory,
    private val errorInterface: ErrorInterface
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return defaultFactory().create(modelClass).apply {
            this.tryCast<AppViewModelInterface>()?.configure(errorInterface)
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return defaultFactory().create(modelClass, extras).apply {
            this.tryCast<AppViewModelInterface>()?.configure(errorInterface)
        }
    }
}

@MainThread
inline fun <reified VM : ViewModel> AppActivity.appViewModels(): Lazy<VM> {
    val factoryPromise = { defaultViewModelProviderFactory }

    return ViewModelLazy(VM::class, { viewModelStore }, { AppViewModelFactory(factoryPromise, this) }, { this.defaultViewModelCreationExtras })
}

@MainThread
inline fun <reified VM : ViewModel> AppFragment<*>.appViewModels(): Lazy<VM> {
    val owner by lazy(LazyThreadSafetyMode.NONE) { this }
    val store = { owner.viewModelStore }
    val extrasProducer = { (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras ?: CreationExtras.Empty }
    val factory = { (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory ?: defaultViewModelProviderFactory }

    return createViewModelLazy(VM::class, store, extrasProducer) { AppViewModelFactory(factory, this) }
}

@MainThread
inline fun <reified VM : ViewModel> BaseBottomSheetDialogFragment.appViewModels(): Lazy<VM> {
    val owner by lazy(LazyThreadSafetyMode.NONE) { this }
    val store = { owner.viewModelStore }
    val extrasProducer = { (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras ?: CreationExtras.Empty }
    val factory = { (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory ?: defaultViewModelProviderFactory }

    return createViewModelLazy(VM::class, store, extrasProducer) { AppViewModelFactory(factory, this) }
}

@MainThread
inline fun <reified VM : ViewModel> AppFragment<*>.appActivityViewModels(): Lazy<VM> {
    val store = { requireActivity().viewModelStore }
    val extraProducer = { requireActivity().defaultViewModelCreationExtras }
    val factory = { requireActivity().defaultViewModelProviderFactory }

    return createViewModelLazy(VM::class, store, extraProducer) { AppViewModelFactory(factory, this) }
}