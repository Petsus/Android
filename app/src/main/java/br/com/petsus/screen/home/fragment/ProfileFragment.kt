package br.com.petsus.screen.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.petsus.R
import br.com.petsus.databinding.FragmentProfileBinding
import br.com.petsus.screen.profile.EditProfileActivity
import br.com.petsus.util.base.fragment.BaseFragment
import com.bumptech.glide.Glide

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.imageProfile?.apply {
            Glide.with(this).load(R.drawable.sample3)
                .circleCrop().into(this)
                .clearOnDetach()
        }

        binding?.profileMenu?.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding?.addressMenu?.setOnClickListener {

        }

        binding?.aboutMenu?.setOnClickListener {

        }

        binding?.logoutMenu?.setOnClickListener {

        }
    }

}