package br.com.petsus.screen.login.start.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import br.com.petsus.R
import br.com.petsus.databinding.FragmentResetPasswordBinding
import br.com.petsus.screen.login.start.LoginViewModel
import br.com.petsus.util.base.fragment.BaseFragment
import br.com.petsus.util.base.fragment.findNavigation
import br.com.petsus.util.base.viewmodel.StringFormatter
import br.com.petsus.util.tool.preventDoubleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetFragment : BaseFragment<FragmentResetPasswordBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentResetPasswordBinding.bind(view).apply {
            root.doOnLayout {
                container.minHeight = root.height
            }

            keyboardController.setOnKeyboardChangeListener { heightDiff ->
                container.minHeight = root.height - heightDiff
            }

            back.setOnClickListener {
                findNavigation()?.dismiss()
            }

            resetPassword.setOnClickListener { reset ->
                reset.preventDoubleClick()
                loading()

                viewModel.resetPassword(
                    email = inputEmail.editText?.text?.toString()
                ).observe(viewLifecycleOwner) {
                    closeLoading()
                    message(StringFormatter(messageId = R.string.send_reset_successfull))
                }
            }
        }
    }
}