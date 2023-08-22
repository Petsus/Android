package br.com.petsus.util.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.widget.FrameLayout
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tapadoo.alerter.Alerter

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(), ErrorInterface, SuccessInterface, LoadingInterface {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener { dialogInterface ->
                val bottomSheetDialog = dialogInterface as? BottomSheetDialog ?: return@setOnShowListener
                val bottomSheetView = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) ?: return@setOnShowListener
                BottomSheetBehavior.from(bottomSheetView).also { sheet ->
                    sheet.skipCollapsed = true
                    sheet.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
    }

    override fun error(message: StringFormatter): ErrorInterface {
        closeLoading()

        val activity = activity ?: return this
        val context = context ?: return this

        Alerter.create(activity)
            .setText(message.parse(context))
            .setIcon(R.drawable.icon_error)
            .setOnHideListener { onErrorClose() }
            .setIconColorFilter(ResourcesCompat.getColor(resources, R.color.md_theme_onErrorContainer, activity.theme))
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
        val activity = activity ?: return this
        val context = context ?: return this

        Alerter.create(activity)
            .setText(message.parse(context))
            .setIcon(R.drawable.icon_error)
            .setIconColorFilter(ResourcesCompat.getColor(resources, R.color.md_theme_onSecondaryContainer, activity.theme))
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
            .show(childFragmentManager, LoadingFragment::class.java.name)
        return this
    }

    override fun closeLoading(closing: Action<Unit>?) {
        childFragmentManager
            .findFragmentByTag(LoadingFragment::class.java.name)
            ?.cast<LoadingFragment>()
            ?.runCatching { close(closing = closing) }
    }
}