package br.com.petsus.util.base.fragment

import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import br.com.petsus.R
import br.com.petsus.util.base.dialog.ErrorInterface
import br.com.petsus.util.base.dialog.LoadingInterface
import br.com.petsus.util.base.dialog.SuccessInterface
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.base.viewmodel.parse
import br.com.petsus.util.global.dialog.LoadingFragment
import br.com.petsus.util.tool.cast
import com.tapadoo.alerter.Alerter

abstract class BaseFragment<T : ViewBinding> : Fragment(), ErrorInterface, SuccessInterface, LoadingInterface {

    protected var binding: T? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun error(message: StringFormatter): ErrorInterface {
        closeLoading()

        val activity = activity ?: return this
        val context = context ?: return this

        Alerter.create(activity)
            .setText(message.parse(context))
            .setIcon(R.drawable.icon_error)
            .setOnHideListener { onErrorClose() }
            .setIconColorFilter(ResourcesCompat.getColor(resources, R.color.md_theme_dark_onErrorContainer, activity.theme))
            .setBackgroundColorRes(R.color.md_theme_dark_errorContainer)
            .setTextAppearance(R.style.AppTheme_Petsus_Text_Error)
            .show()
        return this
    }

    override fun closeError() {
        if (Alerter.isShowing)
            Alerter.hide()
    }

    override fun message(message: StringFormatter): SuccessInterface {
        val activity = activity ?: return this
        val context = context ?: return this

        Alerter.create(activity)
            .setText(message.parse(context))
            .setIcon(R.drawable.icon_error)
            .setIconColorFilter(ResourcesCompat.getColor(resources, R.color.md_theme_dark_onSecondaryContainer, activity.theme))
            .setBackgroundColorRes(R.color.md_theme_dark_secondaryContainer)
            .setTextAppearance(R.style.AppTheme_Petsus_Text_Successful)
            .show()
        return this
    }

    override fun closeMessage() {
        if (Alerter.isShowing)
            Alerter.hide()
    }

    override fun loading(): LoadingInterface {
        LoadingFragment()
            .show(childFragmentManager, LoadingFragment::class.java.name)
        return this
    }

    override fun closeLoading() {
        childFragmentManager
            .findFragmentByTag(LoadingFragment::class.java.name)
            ?.cast<LoadingFragment>()
            ?.runCatching { close() }
    }
}