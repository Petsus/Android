package br.com.petsus.util.global.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import br.com.petsus.R
import br.com.petsus.databinding.FragmentLoadingBinding
import br.com.petsus.util.global.Action
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingFragment @JvmOverloads constructor(
    private val defaultDispatcher: MainCoroutineDispatcher = Dispatchers.Main
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentLoadingBinding.inflate(inflater, container, false).root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.AppTheme_Petsus_DialogBase)
    }

    fun close(closing: Action<Unit>? = null) {
        lifecycleScope.launch(defaultDispatcher) {
            delay(1000)
            runCatching { dismiss() }
            closing?.action(Unit)
        }
    }

}