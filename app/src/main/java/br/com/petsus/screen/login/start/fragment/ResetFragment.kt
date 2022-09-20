package br.com.petsus.screen.login.start.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.petsus.databinding.FragmentResetPasswordBinding
import br.com.petsus.screen.login.start.LoginViewModel
import br.com.petsus.util.base.BaseFragment
import br.com.petsus.util.base.viewmodel.appViewModels
import br.com.petsus.util.tool.preventDoubleClick

class ResetFragment : BaseFragment<FragmentResetPasswordBinding>() {

    private val viewModel: LoginViewModel by appViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.resetPassword?.setOnClickListener {
            it.preventDoubleClick()
            showLoading()

            viewModel.resetPassword(
                email = binding?.inputEmail?.editText?.text?.toString()
            ).observe(viewLifecycleOwner) {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

}