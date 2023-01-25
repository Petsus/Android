package br.com.petsus.screen.profile

import android.os.Bundle
import androidx.activity.viewModels
import br.com.petsus.databinding.ActivityEditProfileBinding
import br.com.petsus.util.base.activity.BaseActivity
import br.com.petsus.util.tool.listenerDismiss

class EditProfileActivity : BaseActivity() {

    private val viewModel: EditProfileViewModel by viewModels()

    private val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loading()

        binding.toolbarEditProfile.listenerDismiss(this)

        viewModel.get().observe(this) { user ->
            closeLoading()
            binding.inputEmail.editText?.setText(user.email)
            binding.inputPhone.editText?.setText(user.phone)
            binding.inputName.editText?.setText(user.name)
        }
    }

}