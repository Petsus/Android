package br.com.petsus.util.global.router

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import br.com.petsus.R

class Navigator private constructor(
    private val container: FragmentContainerView,
    private val activity: AppCompatActivity,
) {

    companion object {
        fun of(host: FragmentContainerView): Navigator {
            return Navigator(
                container = host,
                activity = host.context as AppCompatActivity
            )
        }
    }

    fun show(
        fragment: Fragment,
        addToBack: Boolean = true,
    ) {
        activity.supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.full_enter_fragment, R.anim.full_exit_fragment,
                R.anim.full_pop_enter_fragment, R.anim.full_pop_exit_fragment
            )
            replace(container.id, fragment, fragment::class.java.name)
            if (addToBack)
                addToBackStack(fragment::class.java.name)
        }
    }

    fun dismiss() {
        activity.onBackPressedDispatcher.onBackPressed()
    }

}