package br.com.petsus.screen.home.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import br.com.petsus.databinding.FragmentProfileBinding
import br.com.petsus.screen.about.AboutAppActivity
import br.com.petsus.screen.address.AddressActivity
import br.com.petsus.screen.home.HomeViewModel
import br.com.petsus.screen.login.start.LoginActivity
import br.com.petsus.screen.notification.NotificationActivity
import br.com.petsus.screen.profile.EditProfileActivity
import br.com.petsus.util.base.fragment.AppFragment
import br.com.petsus.util.base.viewmodel.appActivityViewModels
import com.bumptech.glide.Glide

class ProfileFragment : AppFragment<FragmentProfileBinding>() {

    private val viewModel: HomeViewModel by appActivityViewModels()

    private val requestImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val uri = result?.data?.data ?: return@registerForActivityResult
        viewModel.handlerImage(uri = uri).observe(this) { image ->
            val imageView = binding?.imageProfile ?: return@observe
            Glide.with(this)
                .load(image)
                .circleCrop()
                .into(imageView)
        }
    }

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

        viewModel.name().observe(viewLifecycleOwner) { name ->
            binding.nameUser.text = name
        }

        viewModel.currentImage().observe(viewLifecycleOwner) { image ->
            Glide.with(this)
                .load(image)
                .circleCrop()
                .into(binding.imageProfile)
        }

        binding.notificationMenu.setOnClickListener {
            startActivity(
                Intent(requireContext(), NotificationActivity::class.java)
            )
        }

        binding.imageProfile.setOnClickListener {
            requestImage.launch(
                Intent(Intent.ACTION_OPEN_DOCUMENT)
                    .addCategory(Intent.CATEGORY_OPENABLE)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .setType("image/*")
            )
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
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

}