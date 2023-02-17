package br.com.petsus.util.base.fragment

import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import br.com.petsus.util.global.router.Navigator
import br.com.petsus.util.tool.cast

fun Fragment.findNavigation(): Navigator? {
    val rootView = activity?.findViewById<ViewGroup>(android.R.id.content)?.cast<ViewGroup>() ?: return null
    return findViewGroup(rootView)?.run(Navigator::of)
}

private fun findViewGroup(group: ViewGroup): FragmentContainerView? {
    for (index in 0 until  group.childCount) {
        when (val view = group[index]) {
            is FragmentContainerView -> return view
            is ViewGroup -> return findViewGroup(view)
        }
    }

    return null
}