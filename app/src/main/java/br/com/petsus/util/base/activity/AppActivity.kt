package br.com.petsus.util.base.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import br.com.petsus.R
import br.com.petsus.util.base.dialog.ErrorInterface
import br.com.petsus.util.base.dialog.LoadingInterface
import br.com.petsus.util.base.dialog.SuccessInterface
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.base.viewmodel.parse
import br.com.petsus.util.global.Action
import br.com.petsus.util.global.dialog.LoadingFragment
import br.com.petsus.util.tool.cast
import com.tapadoo.alerter.Alerter

abstract class AppActivity : AppCompatActivity(), ErrorInterface, SuccessInterface, LoadingInterface {
    override fun error(message: StringFormatter): ErrorInterface {
        closeLoading()

        Alerter.create(this)
            .setText(message.parse(this))
            .setIcon(R.drawable.icon_error)
            .setOnHideListener { onErrorClose() }
            .setIconColorFilter(ResourcesCompat.getColor(resources, R.color.md_theme_onErrorContainer, theme))
            .setBackgroundColorRes(R.color.md_theme_errorContainer)
            .setTextAppearance(R.style.AppTheme_Petsus_Text_Error)
            .show()
        return this
    }

    override fun closeError() {
        if (Alerter.isShowing)
            Alerter.hide()
    }

    override fun message(message: StringFormatter): SuccessInterface {
        Alerter.create(this)
            .setText(message.parse(this))
            .setIcon(R.drawable.icon_error)
            .setIconColorFilter(ResourcesCompat.getColor(resources, R.color.md_theme_onSecondaryContainer, theme))
            .setBackgroundColorRes(R.color.md_theme_secondaryContainer)
            .setTextAppearance(R.style.AppTheme_Petsus_Text_Successful)
            .show()
        return this
    }

    override fun closeMessage() {
        if (Alerter.isShowing)
            Alerter.hide()
    }

    override fun showLoading(): LoadingInterface {
        LoadingFragment()
            .show(supportFragmentManager, LoadingFragment::class.java.name)
        return this
    }

    override fun closeLoading(closing: Action<Unit>?) {
        supportFragmentManager
            .findFragmentByTag(LoadingFragment::class.java.name)
            ?.cast<LoadingFragment>()
            ?.runCatching { close(closing = closing) }
    }

}