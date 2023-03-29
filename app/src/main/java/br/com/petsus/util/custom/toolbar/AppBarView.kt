package br.com.petsus.util.custom.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import androidx.appcompat.widget.LinearLayoutCompat
import br.com.petsus.R
import br.com.petsus.databinding.CustomAppBarBinding
import br.com.petsus.util.global.Action

class AppBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    deftStyleRes: Int = 0
) : LinearLayoutCompat(context, attrs, deftStyleRes) {

    private val binding: CustomAppBarBinding = CustomAppBarBinding.inflate(LayoutInflater.from(context))

    private var onBackClickListener: Action<AppBarView>? = null

    private val onClickListener: OnClickListener = OnClickListener {
        onBackClickListener?.action(this)
    }

    init {
        addView(binding.root)
        context.obtainStyledAttributes(attrs, R.styleable.AppBarView).apply {
            binding.titleAppBar.text = getString(R.styleable.AppBarView_android_text)
            recycle()
        }
    }

    fun setOnBackClickListener(listener: Action<AppBarView>?) {
        onBackClickListener = listener
        binding.backAppBar.setOnClickListener(listener?.run { onClickListener })
    }

}