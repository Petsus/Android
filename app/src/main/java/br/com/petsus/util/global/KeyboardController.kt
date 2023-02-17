package br.com.petsus.util.global

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import br.com.petsus.util.tool.cast
import java.lang.ref.WeakReference

class KeyboardController(
    private val activity: WeakReference<AppCompatActivity>
) : DefaultLifecycleObserver {

    companion object {
        fun of(from: AppCompatActivity): KeyboardController {
            return KeyboardController(
                activity = WeakReference(from),
            )
        }

        fun of(from: Fragment): KeyboardController {
            return KeyboardController(
                activity = WeakReference(from.requireActivity().cast()),
            )
        }
    }

    private val rootView: View? by lazy {
        activity.get()?.findViewById(android.R.id.content)
    }

    private var onKeyboardChangeListener: Action<Int>? = null

    private val listenerKeyboard = ViewTreeObserver.OnGlobalLayoutListener {
        val visibleBounds = Rect()
        rootView?.apply {
            getWindowVisibleDisplayFrame(visibleBounds)
            onKeyboardChangeListener?.action(this.height - visibleBounds.height())
        }
    }

    init {
        activity.get()?.lifecycle?.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        rootView?.viewTreeObserver?.addOnGlobalLayoutListener(listenerKeyboard)
    }

    override fun onPause(owner: LifecycleOwner) {
        rootView?.viewTreeObserver?.removeOnGlobalLayoutListener(listenerKeyboard)
    }

    fun setOnKeyboardChangeListener(observer: Action<Int>?) {
        onKeyboardChangeListener = observer
    }

}