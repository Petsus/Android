package br.com.petsus.util.custom.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.core.view.isGone
import br.com.petsus.R
import br.com.petsus.databinding.CustomAppBarBinding
import br.com.petsus.util.global.Action
import br.com.petsus.util.tool.inflater

class AppBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    deftStyleRes: Int = 0
) : FrameLayout(context, attrs, deftStyleRes) {

    private val binding: CustomAppBarBinding = CustomAppBarBinding.inflate(inflater)

    private var onBackClickListener: Action<AppBarView>? = null

    private val onClickListener: OnClickListener = OnClickListener {
        onBackClickListener?.action(this)
    }

    var subtitle: String
        get() { return binding.subtitleAppBar.text?.toString() ?: "" }
        set(value) {
            binding.subtitleAppBar.text = value
            binding.subtitleAppBar.isGone = value.isEmpty()
        }

    init {
        addView(binding.root)

        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.AppBarView)
        binding.titleAppBar.text = styledAttributes.getString(R.styleable.AppBarView_title)
        binding.subtitleAppBar.apply {
            text = styledAttributes.getString(R.styleable.AppBarView_subtitle) ?: ""
            isGone = text.isNullOrEmpty()
        }

        styledAttributes.recycle()
    }

    fun setOnBackClickListener(listener: Action<AppBarView>?) {
        onBackClickListener = listener
        binding.backAppBar.setOnClickListener(listener?.run { onClickListener })
    }

}