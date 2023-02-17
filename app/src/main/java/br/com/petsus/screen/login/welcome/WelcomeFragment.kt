package br.com.petsus.screen.login.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.petsus.databinding.FragmentWelcomeBinding
import br.com.petsus.screen.login.start.fragment.CreateAccountFragment
import br.com.petsus.screen.login.start.fragment.LoginFragment
import br.com.petsus.util.base.fragment.BaseFragment
import br.com.petsus.util.base.fragment.findNavigation

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentWelcomeBinding.bind(view).apply {
            loginAccount.setOnClickListener {
                findNavigation()?.show(fragment = LoginFragment(), addToBack = false)
            }
            createAccount.setOnClickListener {
                findNavigation()?.show(fragment = CreateAccountFragment())
            }
        }
    }
}