package br.com.petsus.screen.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.petsus.databinding.FragmentProfileBinding
import br.com.petsus.util.base.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.profileMenu?.setOnClickListener {

        }
    }

}