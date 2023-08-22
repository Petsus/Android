package br.com.petsus.util.custom.card

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import br.com.petsus.R
import br.com.petsus.databinding.CustomCardAnimalBinding
import br.com.petsus.util.tool.inflater

class CardDivider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = CustomCardAnimalBinding.inflate(inflater)

    init {
        addView(binding.root)

        binding.dividerContainer.background = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider)).run {
            val divider = getDrawable(0)
            recycle()
            return@run divider
        }

        binding.nameContainer.text = context.obtainStyledAttributes(attrs, R.styleable.CardDivider).run {
            val title = getString(R.styleable.CardDivider_android_text)
            recycle()
            return@run title
        }

        binding.root.setOnClickListener {
            isExpanded = !isExpanded

            binding.toggleExpanded
                .animate()
                .rotation(if (!isExpanded) 180f else 0f)
                .start()
        }
    }

    override fun addView(child: View?) {
        if (child == binding.root)
            super.addView(child)
        else
            binding.contentContainer.addView(child)
    }

    override fun addView(child: View?, index: Int) {
        if (child == binding.root)
            super.addView(child, index)
        else
            binding.contentContainer.addView(child, index)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (child == binding.root)
            super.addView(child, params)
        else
            binding.contentContainer.addView(child, params)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        if (child == binding.root)
            super.addView(child, width, height)
        else
            binding.contentContainer.addView(child, width, height)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child == binding.root)
            super.addView(child, index, params)
        else
            binding.contentContainer.addView(child, index, params)
    }

    private var isExpanded: Boolean
        set(value) { binding.contentContainer.isGone = value }
        get() = binding.contentContainer.isGone

}