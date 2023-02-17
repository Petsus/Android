package br.com.petsus.util.custom.loading

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
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
        loader.isIndeterminate = true
        addView(loader, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER))
        background = ColorDrawable(ContextCompat.getColor(context, R.color.md_theme_background))
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