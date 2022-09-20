package br.com.petsus.screen.login.start.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.petsus.databinding.FragmentLoginCreateUserBinding
import br.com.petsus.util.base.BaseFragment

class CreateAccountFragment : BaseFragment<FragmentLoginCreateUserBinding>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginCreateUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}