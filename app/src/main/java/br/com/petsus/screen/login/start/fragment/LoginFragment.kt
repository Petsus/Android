package br.com.petsus.screen.login.start.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import br.com.petsus.databinding.FragmentLoginBinding
import br.com.petsus.screen.login.start.LoginViewModel
import br.com.petsus.screen.home.HomeActivity
import br.com.petsus.util.base.fragment.BaseFragment
import br.com.petsus.util.base.fragment.findNavigation
import br.com.petsus.util.tool.preventDoubleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureView()
    }

    private fun configureView() {
        keyboardController.setOnKeyboardChangeListener { heightDiff ->
            binding?.apply {
                container.minHeight = root.height - heightDiff
            }
        }
        binding?.apply {
            root.doOnLayout {
                container.minHeight = root.height
            }
            login.setOnClickListener { currentView ->
                currentView.preventDoubleClick()
                loading()

                viewModel.login(
                    email = binding?.inputEmail?.editText?.text?.toString(),
                    password = binding?.inputPassword?.editText?.text?.toString()
                ).observe(viewLifecycleOwner) {
                    startActivity(
                        Intent(requireContext(), HomeActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }
            createAccount.setOnClickListener {
                findNavigation()?.show(fragment = CreateAccountFragment())
            }
            resetPassword.setOnClickListener {
                findNavigation()?.show(fragment = ResetFragment())
            }
        }
    }
}