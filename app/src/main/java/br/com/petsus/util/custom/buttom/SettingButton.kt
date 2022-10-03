package br.com.petsus.util.custom.buttom

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import br.com.petsus.R
import br.com.petsus.databinding.CustomSettingButtonBinding
import br.com.petsus.util.tool.inflater

class SettingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context, attrs, defStyleAttr, defStyleRes
) {

    private val binding: CustomSettingButtonBinding = CustomSettingButtonBinding.inflate(inflater)

    init {
        addView(binding.root)

        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SettingButton)

        if (styledAttributes.hasValue(R.styleable.SettingButton_title))
            binding.settingButtonTitle.text = styledAttributes.getText(R.styleable.SettingButton_title)

        if (styledAttributes.hasValue(R.styleable.SettingButton_icon))
            binding.settingButtonIcon.setImageDrawable(styledAttributes.getDrawable(R.styleable.SettingButton_icon))

        styledAttributes.recycle()
    }

}