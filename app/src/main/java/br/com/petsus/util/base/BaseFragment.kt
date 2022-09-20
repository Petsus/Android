package br.com.petsus.util.base

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    protected var binding: T? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    open fun showLoading() {
    }

    open fun dismissLoading() {
    }

}