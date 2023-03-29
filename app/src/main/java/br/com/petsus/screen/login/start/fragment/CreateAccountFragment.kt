package br.com.petsus.screen.login.start.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import br.com.petsus.databinding.FragmentLoginCreateUserBinding
import br.com.petsus.screen.login.start.LoginViewModel
import br.com.petsus.screen.home.HomeActivity
import br.com.petsus.util.base.fragment.BaseFragment
import br.com.petsus.util.base.fragment.findNavigation
import br.com.petsus.util.tool.preventDoubleClick
import br.com.petsus.util.tool.text
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : BaseFragment<FragmentLoginCreateUserBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginCreateUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            keyboardController.setOnKeyboardChangeListener { heightDiff ->
                container.minHeight = root.height - heightDiff
            }
            root.doOnLayout {
                container.minHeight = root.height
            }
            back.setOnClickListener { findNavigation()?.dismiss() }
            createAccount.setOnClickListener {
                it.preventDoubleClick()
                loading()

                viewModel.createUser(
                    name = binding?.inputName?.text,
                    email = binding?.inputEmail?.text,
                    phone = binding?.inputPhone?.text,
                    password = binding?.inputPassword?.text
                ).observe(viewLifecycleOwner) {
                    closeLoading()
                    context?.apply {
                        startActivity(
                            Intent(this, HomeActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    }
                }
            }
        }
    }

}