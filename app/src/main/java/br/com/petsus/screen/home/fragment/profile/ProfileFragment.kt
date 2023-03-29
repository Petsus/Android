package br.com.petsus.screen.home.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import br.com.petsus.R
import br.com.petsus.databinding.FragmentProfileBinding
import br.com.petsus.screen.about.AboutAppActivity
import br.com.petsus.screen.address.list.AddressActivity
import br.com.petsus.screen.home.HomeViewModel
import br.com.petsus.screen.login.start.LoginActivity
import br.com.petsus.screen.profile.EditProfileActivity
import br.com.petsus.util.base.fragment.BaseFragment
import com.bumptech.glide.Glide

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            configureView(binding = this)
        }
    }

    private fun configureView(binding: FragmentProfileBinding) {
        Glide.with(this)
            .load(R.drawable.sample3)
            .circleCrop()
            .into(binding.imageProfile)
            .clearOnDetach()

        viewModel.name().observe(viewLifecycleOwner) { name ->
            binding.nameUser.text = name
        }

        binding.profileMenu.setOnClickListener {
            startActivity(
                Intent(requireContext(), EditProfileActivity::class.java)
            )
        }

        binding.addressMenu.setOnClickListener {
            context?.apply {
                startActivity(
                    Intent(this, AddressActivity::class.java)
                )
            }
        }

        binding.aboutMenu.setOnClickListener {
            context?.apply {
                startActivity(
                    Intent(this, AboutAppActivity::class.java)
                )
            }
        }

        binding.logoutMenu.setOnClickListener {
            viewModel.logout()
            context?.apply {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
            }
        }
    }

}