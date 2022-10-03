package br.com.petsus.util.custom.loading

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import br.com.petsus.R
import com.google.android.material.progressindicator.CircularProgressIndicator

class BaseLoading @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context, attrs, defStyleAttr, defStyleRes
) {

    private val loader = CircularProgressIndicator(context)

    init {
        addView(loader)
        background = ColorDrawable(ContextCompat.getColor(context, R.color.md_theme_dark_background))
    }

    fun show() {
        isInvisible = false
        loader.show()
    }

    fun hide() {
        isInvisible = true
        loader.hide()
    }

}